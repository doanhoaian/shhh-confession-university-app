package vn.dihaver.tech.shhh.confession.core.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.dihaver.tech.shhh.confession.core.domain.model.FeedItem
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.FeedPageDto
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.InteractionStatusDto
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.PostDto

interface FeedRepository {
    fun getFeed(
        topicValue: String?
    ): Flow<PagingData<FeedItem>>

    suspend fun getPostIdsFromServer(
        token: String,
        schoolId: Int,
        topic: String?,
        limit: Int,
        lastPostId: String?,
        lastScore: Double?
    ): FeedPageDto

    suspend fun getPostsContentFromServer(token: String, ids: List<String>): List<PostDto>

    suspend fun getUserInteractionsFromServer(
        token: String,
        ids: List<String>
    ): Map<String, InteractionStatusDto>
}