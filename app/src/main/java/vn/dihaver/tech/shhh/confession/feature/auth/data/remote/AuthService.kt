package vn.dihaver.tech.shhh.confession.feature.auth.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import vn.dihaver.tech.shhh.confession.core.data.remote.BaseResponse
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.AliasDto
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.CheckEmailDto
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.CheckEmailRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.LoginRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.ResetPasswordRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.SchoolDto
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.SendOtpDto
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.SendOtpRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.UpdateAliasDto
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.UpdateAliasRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.UpdateSchoolRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.UserSessionDto
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.VerifyOtpDto
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.VerifyOtpRequest

interface AuthService {

    @GET("users/aliases")
    suspend fun getAllAliases(@Header("Authorization") authHeader: String): Response<BaseResponse<List<AliasDto>>>

    @GET("users/schools")
    suspend fun getAllSchools(@Header("Authorization") authHeader: String): Response<BaseResponse<List<SchoolDto>>>

    @POST("users/auth/check-email")
    suspend fun checkEmail(@Body request: CheckEmailRequest): Response<BaseResponse<CheckEmailDto>>

    @POST("users/auth/login-or-register")
    suspend fun loginOrRegister(@Header("Authorization") authHeader: String, @Body request: LoginRequest): Response<BaseResponse<UserSessionDto>>

    @POST("otps/send")
    suspend fun sendOtp(@Body request: SendOtpRequest): Response<BaseResponse<SendOtpDto>>

    @POST("otps/verify")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<BaseResponse<VerifyOtpDto>>

    @PATCH("users/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<BaseResponse<Any>>

    @PATCH("users/update-alias")
    suspend fun updateAlias(@Header("Authorization") authHeader: String, @Body request: UpdateAliasRequest): Response<BaseResponse<UpdateAliasDto>>

    @PATCH("users/update-school")
    suspend fun updateSchool(@Header("Authorization") authHeader: String, @Body request: UpdateSchoolRequest): Response<BaseResponse<Any>>
}
