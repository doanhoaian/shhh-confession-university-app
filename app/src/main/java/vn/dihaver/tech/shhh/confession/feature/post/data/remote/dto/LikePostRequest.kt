package vn.dihaver.tech.shhh.confession.feature.post.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LikePostRequest(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("post_id")
    val postId: String
)
