package vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("id")
    val uid: String,
    val email: String,
    @SerializedName("password_hash")
    val passwordHash: String,
    @SerializedName("login_method")
    val loginMethod: LoginMethod,
    @SerializedName("device_info")
    val deviceInfo: Map<String, Any>,
    val platform: String = "Android"
)

enum class LoginMethod {
    @SerializedName("email")
    Email,
    @SerializedName("google")
    Google
}
