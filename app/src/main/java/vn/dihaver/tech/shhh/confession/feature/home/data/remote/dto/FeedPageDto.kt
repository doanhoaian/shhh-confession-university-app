package vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FeedPageDto(
    @SerializedName("items")
    val items: List<String>,

    @SerializedName("next_cursor")
    val nextCursor: NextCursorDto?
)