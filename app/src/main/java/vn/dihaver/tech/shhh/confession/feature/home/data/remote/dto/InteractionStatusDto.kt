package vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto

import com.google.gson.annotations.SerializedName

data class InteractionStatusDto(
    @SerializedName("is_liked")
    val isLiked: Boolean,
    @SerializedName("is_disliked")
    val isDisliked: Boolean
)
