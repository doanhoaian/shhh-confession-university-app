package vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto

data class VerifyOtpRequest(
    val email: String,
    val otp: String,
    val type: OtpType
)
