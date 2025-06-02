package vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    val email: String,
    val password: String,
    @SerializedName("password_hash")
    val passwordHash: String
)
