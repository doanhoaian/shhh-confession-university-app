package vn.dihaver.tech.shhh.confession.core.domain.repository

import vn.dihaver.tech.shhh.confession.core.domain.model.Alias
import vn.dihaver.tech.shhh.confession.feature.auth.model.CheckEmail
import vn.dihaver.tech.shhh.confession.core.domain.model.School
import vn.dihaver.tech.shhh.confession.core.domain.model.UserSession
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.LoginRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.ResetPasswordRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.SendOtpRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.UpdateAliasRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.UpdateSchoolRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.VerifyOtpRequest

interface AuthRepository {
    suspend fun checkEmail(email: String): CheckEmail
    suspend fun loginOrRegister(request: LoginRequest): UserSession
    suspend fun sendOtp(request: SendOtpRequest): Boolean
    suspend fun verifyOtp(request: VerifyOtpRequest): Boolean
    suspend fun getAllAliases(): List<Alias>
    suspend fun getAllSchools(): List<School>
    suspend fun resetPassword(request: ResetPasswordRequest)
    suspend fun updateAlias(request: UpdateAliasRequest): Int
    suspend fun updateSchool(request: UpdateSchoolRequest)
    suspend fun logoutAuth(): Boolean
}
