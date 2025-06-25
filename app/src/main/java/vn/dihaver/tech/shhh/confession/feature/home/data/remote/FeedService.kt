package vn.dihaver.tech.shhh.confession.feature.home.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import vn.dihaver.tech.shhh.confession.core.data.remote.BaseResponse
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.FeedPageDto
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.InteractionStatusDto
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.PostDto
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.PostIdsRequest

interface FeedService {

    @GET("posts/feed/ids")
    suspend fun getPostIds(
        @Header("Authorization") authHeader: String,
        @Query("school_id") schoolId: Int,
        @Query("topic_value") topicValue: String? = "",
        @Query("limit") limit: Int,
        @Query("last_post_id") lastPostId: String? = null,
        @Query("last_score") lastScore: Double? = null
    ): Response<BaseResponse<FeedPageDto>>

    @POST("posts/feed/content")
    suspend fun getFeedContent(
        @Header("Authorization") authHeader: String,
        @Body postIdsRequest: PostIdsRequest
    ): Response<BaseResponse<List<PostDto>>>

    @POST("me/interactions")
    suspend fun getUserInteractions(
        @Header("Authorization") authHeader: String,
        @Body postIdsRequest: PostIdsRequest
    ): Response<BaseResponse<Map<String, InteractionStatusDto>>>
}