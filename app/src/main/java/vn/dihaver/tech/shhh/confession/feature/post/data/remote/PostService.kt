package vn.dihaver.tech.shhh.confession.feature.post.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import vn.dihaver.tech.shhh.confession.core.data.remote.BaseResponse
import vn.dihaver.tech.shhh.confession.feature.post.data.remote.dto.CreatePostDto

interface PostService {
    @Multipart
    @POST("posts/create")
    suspend fun createPost(
        @Header("Authorization") authHeader: String,
        @Part("user_id") userId: RequestBody,
        @Part("school_id") schoolId: RequestBody,
        @Part("content") content: RequestBody,
        @Part("comment_permission") commentPermission: RequestBody,
        @Part("view_permission") viewPermission: RequestBody,
        @Part images: List<MultipartBody.Part>? = null
    ): Response<BaseResponse<CreatePostDto>>
}