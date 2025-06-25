package vn.dihaver.tech.shhh.confession.feature.post.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreatePostDto(
    @SerializedName("post_id")
    val postId: String,
    val status: String,
    @SerializedName("hidden_reason")
    val hiddenReason: String?,
    @SerializedName("created_at")
    val createdAt: String
)