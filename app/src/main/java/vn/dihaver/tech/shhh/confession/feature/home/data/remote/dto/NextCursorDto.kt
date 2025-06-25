package vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NextCursorDto(
    @SerializedName("last_post_id")
    val lastPostId: String?,

    @SerializedName("last_score")
    val lastScore: Double?
)