package vn.dihaver.tech.shhh.confession.core.data.remote

import com.google.gson.annotations.SerializedName

data class BaseResponse<T : Any>(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T? = null
)