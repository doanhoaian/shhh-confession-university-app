package vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SendOtpRequest(
    val email: String,
    val type: OtpType
)

enum class OtpType(@SerializedName("value") val value: String) {
    @SerializedName("verify_email")
    VERIFY_EMAIL("verify_email"),

    @SerializedName("2fa_login")
    TWO_FA_LOGIN("2fa_login"),

    @SerializedName("reset_password")
    RESET_PASSWORD("reset_password");
}
