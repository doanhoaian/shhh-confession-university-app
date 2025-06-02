package vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CheckEmailDto(
    @SerializedName("is_exists")
    val isExists: Boolean,
    val providers: List<String>
)
