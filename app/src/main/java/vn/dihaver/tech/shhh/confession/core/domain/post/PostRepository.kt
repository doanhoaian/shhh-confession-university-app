package vn.dihaver.tech.shhh.confession.core.domain.post

import android.net.Uri
import vn.dihaver.tech.shhh.confession.feature.post.data.remote.dto.CreatePostDto
import vn.dihaver.tech.shhh.confession.feature.post.data.remote.dto.CreatePostRequest
import vn.dihaver.tech.shhh.confession.feature.post.data.remote.dto.LikePostRequest

interface PostRepository {
    suspend fun createPost(request: CreatePostRequest, imageUris: List<Uri>): CreatePostDto
    suspend fun likePost(request: LikePostRequest)
}