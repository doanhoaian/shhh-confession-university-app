package vn.dihaver.tech.shhh.confession.feature.home.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import vn.dihaver.tech.shhh.confession.core.data.local.database.AppDatabase
import vn.dihaver.tech.shhh.confession.core.data.local.datastore.SessionManager
import vn.dihaver.tech.shhh.confession.core.data.local.firebase.AuthManager
import vn.dihaver.tech.shhh.confession.core.data.repository.BaseRepository
import vn.dihaver.tech.shhh.confession.core.domain.model.FeedItem
import vn.dihaver.tech.shhh.confession.core.domain.repository.FeedRepository
import vn.dihaver.tech.shhh.confession.core.util.UnauthorizedException
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.InteractionEntity
import vn.dihaver.tech.shhh.confession.feature.home.data.pager.FeedRemoteMediator
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.FeedService
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.FeedPageDto
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.InteractionStatusDto
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.PostDto
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.PostIdsRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepositoryImpl @Inject constructor(
    private val authManager: AuthManager,
    private val appDatabase: AppDatabase,
    private val feedService: FeedService,
    private val sessionManager: SessionManager
) : FeedRepository, BaseRepository() {

    private val remoteKeyDao = appDatabase.feedRemoteKeyDao()

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override fun getFeed(
        topicValue: String?
    ): Flow<PagingData<FeedItem>> {
        return sessionManager.userSession.flatMapLatest { session ->
            val schoolId =
                session?.schoolId ?: return@flatMapLatest flowOf(PagingData.empty<FeedItem>())

            val feedIdentifier = "schoolId=$schoolId&topic=${topicValue ?: ""}"

            val pagerFactory = {
                Pager(
                    config = PagingConfig(pageSize = 15, prefetchDistance = 0),
                    remoteMediator = FeedRemoteMediator(
                        feedIdentifier = feedIdentifier,
                        schoolId = schoolId,
                        topicValue = topicValue,
                        currentUserId = authManager.uid,
                        feedRepository = this,
                        appDatabase = appDatabase,
                        getAuthToken = {
                            authManager.getBearerToken()
                                ?: throw UnauthorizedException("User token is null")
                        }
                    ),
                    pagingSourceFactory = {
                        appDatabase.feedRemoteKeyDao().pagingSource(
                            version = feedIdentifier,
                            currentUserId = authManager.uid
                        )
                    }
                ).flow
            }

            flow {
//                handleUserSwitchAndCache(schoolId, topicValue)
                emitAll(pagerFactory())
            }.map { pagingData ->
                pagingData.map { it.toDomain() }
            }
        }
    }

    override suspend fun getPostIdsFromServer(
        token: String,
        schoolId: Int,
        topic: String?,
        limit: Int,
        lastPostId: String?,
        lastScore: Double?
    ): FeedPageDto {
        return safeApiCall {
            feedService.getPostIds(
                authHeader = token,
                schoolId = schoolId,
                topicValue = topic,
                limit = limit,
                lastPostId = lastPostId,
                lastScore = lastScore
            )
        }
    }

    override suspend fun getPostsContentFromServer(
        token: String,
        ids: List<String>
    ): List<PostDto> {
        val postIdsRequest = PostIdsRequest(
            postIds = ids
        )

        return safeApiCall {
            feedService.getFeedContent(
                authHeader = token,
                postIdsRequest = postIdsRequest
            )
        }
    }

    override suspend fun getUserInteractionsFromServer(
        token: String,
        ids: List<String>
    ): Map<String, InteractionStatusDto> {
        val postIdsRequest = PostIdsRequest(
            postIds = ids
        )

        return safeApiCall {
            feedService.getUserInteractions(
                authHeader = token,
                postIdsRequest = postIdsRequest
            )
        }
    }

    /**
     * Logic "siêu thông minh" để xử lý việc đổi người dùng trên cache cũ.
     */
    private suspend fun handleUserSwitchAndCache(schoolId: Int, topicValue: String?) {
        val currentUserId = authManager.uid
        val feedIdentifier = "schoolId=$schoolId&topic=${topicValue ?: ""}"
        val latestKey = remoteKeyDao.getLatestRemoteKeyForFeed(feedIdentifier) ?: return

        val isCacheStaleForUser = latestKey.cachedForUserId != currentUserId
        val isCacheTTLExpired =
            (System.currentTimeMillis() - latestKey.createdAt) >= (5 * 60 * 1000)

        // Chỉ hành động khi cache còn hạn nhưng sai người dùng
        if (isCacheStaleForUser && !isCacheTTLExpired) {
            try {
                // Lấy danh sách ID từ cache
                val postIds = remoteKeyDao.getPostIdsByFeedIdentifier(feedIdentifier)
                if (postIds.isEmpty()) return

                // Chỉ gọi API lấy tương tác
                if (currentUserId != null) {
                    val token = authManager.getBearerToken()
                    if (token.isNullOrEmpty()) {
                        return
                    }

                    val interactions = getUserInteractionsFromServer(token, postIds)

                    val interactionEntities = interactions.map { (postId, dto) ->
                        InteractionEntity(postId, currentUserId, dto.isLiked, dto.isDisliked)
                    }
                    interactionEntities.forEach {
                        appDatabase.interactionDao().upsertInteraction(it)
                    }
                }

                // Cập nhật lại cache metadata
                remoteKeyDao.updateFeedUserAndTimestamp(
                    feedIdentifier = feedIdentifier,
                    newUserId = currentUserId,
                    newTimestamp = System.currentTimeMillis()
                )

            } catch (e: Exception) {
                // Nothing
            }
        }
    }
}