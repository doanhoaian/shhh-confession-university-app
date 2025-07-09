package vn.dihaver.tech.shhh.confession.feature.home.data.pager

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import vn.dihaver.tech.shhh.confession.core.data.local.database.AppDatabase
import vn.dihaver.tech.shhh.confession.core.domain.repository.FeedRepository
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.FeedItemAndEntities
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.FeedPageMetadataEntity
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.FeedRemoteKeyEntity
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.InteractionEntity
import vn.dihaver.tech.shhh.confession.feature.home.data.mapper.toEntity
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator(
    private val feedIdentifier: String,
    private val schoolId: Int,
    private val topicValue: String?,
    private val currentUserId: String?,

    // Dependency
    private val feedRepository: FeedRepository,
    private val appDatabase: AppDatabase,
    private val getAuthToken: suspend () -> String
) : RemoteMediator<Int, FeedItemAndEntities>() {

    private val postDao = appDatabase.postDao()
    private val interactionDao = appDatabase.interactionDao()
    private val remoteKeyDao = appDatabase.feedRemoteKeyDao()
    private val pageMetadataDao = appDatabase.feedPageMetadataDao()

    companion object {
        private const val TAG = "AAA-FeedRemoteMediator"

        // Cache sẽ hết hạn sau 5 phút
        private val FEED_CACHE_TTL_IN_MILLIS = TimeUnit.MINUTES.toMillis(5)
    }

    init {
        // Log khi một instance mới của Mediator được tạo.
        // Nếu log này xuất hiện mỗi khi bạn vào lại màn hình, có thể Pager đang bị tạo lại không cần thiết.
        Log.d(
            TAG,
            "🟢 FeedRemoteMediator CREATED. Identifier: '$feedIdentifier', UserID: '$currentUserId'"
        )
    }

    override suspend fun initialize(): InitializeAction {
        Log.d(TAG, "[initialize] 🔵 Checking cache state for identifier: '$feedIdentifier'")

        // 1. Kiểm tra xem có remote key nào trong DB không
        val latestKey = remoteKeyDao.getLatestRemoteKeyForFeed(feedIdentifier)
        if (latestKey == null) {
            Log.d(TAG, "[initialize] 🟡 Result: No remote key found. LAUNCHING INITIAL REFRESH.")
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }
        Log.d(TAG, "[initialize] Found latest key created at: ${latestKey.createdAt}")

        // 2. Kiểm tra cache hết hạn
        val cacheAge = System.currentTimeMillis() - latestKey.createdAt
        val isCacheExpired = cacheAge >= FEED_CACHE_TTL_IN_MILLIS
        Log.d(
            TAG,
            "[initialize] Checking cache expiration: age=${cacheAge}ms, TTL=${FEED_CACHE_TTL_IN_MILLIS}ms. Is expired? $isCacheExpired"
        )

        if (isCacheExpired) {
            Log.d(TAG, "[initialize] 🟡 Result: Cache is EXPIRED. LAUNCHING INITIAL REFRESH.")
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }

        // 3. Kiểm tra cache có đúng của người dùng hiện tại không
        Log.d(
            TAG,
            "[initialize] Checking user match: Cached User='${latestKey.cachedForUserId}', Current User='${currentUserId}'. Is match? ${latestKey.cachedForUserId == currentUserId}"
        )
        if (latestKey.cachedForUserId != currentUserId) {
            Log.d(TAG, "[initialize] 🟡 Result: User mismatch. LAUNCHING INITIAL REFRESH.")
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }

        // 4. Nếu mọi thứ đều ổn
        Log.d(TAG, "[initialize] ✅ Result: Cache is VALID. SKIPPING INITIAL REFRESH.")
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FeedItemAndEntities>
    ): MediatorResult {
        Log.d(TAG, "[load] 🔵 Starting load with type: $loadType")

        try {
            // 1. Lấy CURSOR
            val (lastPostId, lastScore) = when (loadType) {
                LoadType.REFRESH -> {
                    Log.d(TAG, "[load] LoadType is REFRESH. Resetting cursor.")
                    null to null
                }

                LoadType.PREPEND -> {
                    Log.d(TAG, "[load] LoadType is PREPEND. Reached end of pagination.")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val metadata = pageMetadataDao.getMetadataForFeed(feedIdentifier)
                    Log.d(TAG, "[load] LoadType is APPEND. Metadata from DB: $metadata")
                    if (metadata?.nextPostId == null || metadata.nextScore == null) {
                        Log.d(TAG, "[load] No next cursor in metadata. Reached end of pagination.")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    Log.d(
                        TAG,
                        "[load] Using cursor for APPEND: postId=${metadata.nextPostId}, score=${metadata.nextScore}"
                    )
                    metadata.nextPostId to metadata.nextScore
                }
            }

            Log.d(TAG, "[load] 📡 Calling API...")
            val token = getAuthToken()
            val response = feedRepository.getPostIdsFromServer(
                token,
                schoolId,
                topicValue,
                state.config.pageSize,
                lastPostId,
                lastScore
            )
            val postIds = response.items
            Log.d(TAG, "[load] 📡 API response received. Post count: ${postIds.size}")

            if (postIds.isEmpty()) {
                if (loadType == LoadType.REFRESH) {
                    // Nếu là REFRESH mà không có item nào, ta vẫn nên xóa cache cũ.
                    appDatabase.withTransaction {
                        remoteKeyDao.clearRemoteKeysByVersion(feedIdentifier)
                        pageMetadataDao.clearMetadata(feedIdentifier)
                    }
                }
                Log.d(TAG, "[load] ✅ API returned 0 posts. End of pagination reached.")
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            Log.d(TAG, "[load] 📡 Fetching content and interactions for ${postIds.size} posts...")
            val (postDtos, interactionsMap) = coroutineScope {
                val contentsDeferred =
                    async { feedRepository.getPostsContentFromServer(token, postIds) }
                val interactionsDeferred = async {
                    if (currentUserId != null) feedRepository.getUserInteractionsFromServer(
                        token,
                        postIds
                    ) else null
                }
                contentsDeferred.await() to (interactionsDeferred.await() ?: emptyMap())
            }
            Log.d(
                TAG,
                "[load] 💾 Fetched content for ${postDtos.size} posts and ${interactionsMap.size} interactions."
            )

            Log.d(TAG, "[load] 💾 Starting database transaction for identifier: '$feedIdentifier'")
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Log.d(
                        TAG,
                        "[Transaction] ❗ LoadType is REFRESH. Attempting to clear old data..."
                    )
                    remoteKeyDao.clearRemoteKeysByVersion(feedIdentifier)
                    pageMetadataDao.clearMetadata(feedIdentifier)
                    Log.d(TAG, "[Transaction] ✔️ Cleared old data.")
                }

                val postEntities = postDtos.map { it.toEntity() }
                val interactionEntities = if (currentUserId != null) {
                    postDtos.mapNotNull { postDto ->
                        interactionsMap[postDto.id]?.let { interactionDto ->
                            InteractionEntity(
                                postId = postDto.id,
                                userId = currentUserId,
                                isLiked = interactionDto.isLiked,
                                isDisliked = interactionDto.isDisliked
                            )
                        }
                    }
                } else emptyList()

                postDao.upsertPosts(postEntities)
                interactionEntities.forEach { interactionDao.upsertInteraction(it) }

                val orderStartIndex =
                    if (loadType == LoadType.REFRESH) 0 else state.pages.sumOf { it.data.size }
                val newTimestamp = System.currentTimeMillis() // Tạo timestamp một lần duy nhất

                val remoteKeyEntities = postIds.mapIndexed { index, postId ->
                    FeedRemoteKeyEntity(
                        postId = postId,
                        version = feedIdentifier,
                        cachedForUserId = currentUserId,
                        orderInFeed = orderStartIndex + index,
                        createdAt = newTimestamp // Sử dụng timestamp đã tạo
                    )
                }

                if (remoteKeyEntities.isNotEmpty()) {
                    Log.d(
                        TAG,
                        "[Transaction] ❗ Attempting to insert ${remoteKeyEntities.size} new remote keys."
                    )
                    Log.d(
                        TAG,
                        "[Transaction] Sample new key: postId=${remoteKeyEntities.first().postId}, createdAt=${remoteKeyEntities.first().createdAt}"
                    )
                    remoteKeyDao.insertAll(remoteKeyEntities)
                    Log.d(TAG, "[Transaction] ✔️ Inserted new remote keys.")
                } else {
                    Log.w(TAG, "[Transaction] ⚠️ Warning: No remote key entities to insert.")
                }

                pageMetadataDao.upsert(
                    FeedPageMetadataEntity(
                        feedIdentifier = feedIdentifier,
                        nextPostId = response.nextCursor?.lastPostId,
                        nextScore = response.nextCursor?.lastScore
                    )
                )
            }
            Log.d(TAG, "[load] 💾 Database transaction completed successfully.")

            val endOfPaginationReached = postIds.size < state.config.pageSize
            Log.d(TAG, "[load] ✅ Load successful. End of pagination: $endOfPaginationReached")
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            Log.e(TAG, "[load] 🔴 ERROR: IOException.", e)
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e(TAG, "[load] 🔴 ERROR: HttpException.", e)
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            Log.e(TAG, "[load] 🔴 ERROR: Unexpected exception.", e)
            return MediatorResult.Error(e)
        }
    }
}