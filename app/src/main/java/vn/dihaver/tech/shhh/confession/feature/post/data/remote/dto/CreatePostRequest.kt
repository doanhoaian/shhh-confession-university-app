package vn.dihaver.tech.shhh.confession.feature.post.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreatePostRequest(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("school_id")
    val schoolId: String,
    val content: String,
    @SerializedName("comment_permission")
    val commentPermission: String,
    @SerializedName("view_permission")
    val viewPermission: String
)
