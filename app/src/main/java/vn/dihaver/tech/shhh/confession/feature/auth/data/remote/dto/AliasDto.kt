package vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AliasDto(
    val id: String,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("image_url")
    val imageUrl: String
)
