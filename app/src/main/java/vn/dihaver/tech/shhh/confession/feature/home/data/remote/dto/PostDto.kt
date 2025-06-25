package vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PostDto(
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("school_id")
    val schoolId: Int,

    @SerializedName("post_type")
    val postType: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("display_name")
    val displayName: String,

    @SerializedName("school_short_name")
    val schoolShortName: String,

    @SerializedName("content")
    val content: String,
    @SerializedName("images")
    val images: List<String>,

    @SerializedName("status")
    val status: String,
    @SerializedName("comment_permission")
    val commentPermission: String,
    @SerializedName("view_permission")
    val viewPermission: String,

    @SerializedName("total_like")
    val totalLike: Long,
    @SerializedName("total_dislike")
    val totalDislike: Long,
    @SerializedName("total_comment")
    val totalComment: Long,

    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)