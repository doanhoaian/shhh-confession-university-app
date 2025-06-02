package vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SchoolDto(
    val id: Int,
    val name: String,
    @SerializedName("short_name")
    val shortName: String,
    @SerializedName("image_url")
    val imageUrl: String
)
