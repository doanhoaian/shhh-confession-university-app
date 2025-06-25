package vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PostIdsRequest(
    @SerializedName("post_ids")
    val postIds: List<String>
)