package vn.dihaver.tech.shhh.confession.feature.auth.data.repository

import vn.dihaver.tech.shhh.confession.core.data.local.firebase.AuthManager
import vn.dihaver.tech.shhh.confession.core.data.repository.BaseRepository
import vn.dihaver.tech.shhh.confession.core.domain.auth.AuthRepository
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.Alias
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.CheckEmail
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.School
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.UserSession
import vn.dihaver.tech.shhh.confession.core.util.UnauthorizedException
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.AliasDao
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.SchoolDao
import vn.dihaver.tech.shhh.confession.feature.auth.data.mapper.toDomain
import vn.dihaver.tech.shhh.confession.feature.auth.data.mapper.toEntity
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.AuthService
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.CheckEmailRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.LoginRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.ResetPasswordRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.SendOtpRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.UpdateAliasRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.UpdateSchoolRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.VerifyOtpRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val aliasDao: AliasDao,
    private val schoolDao: SchoolDao,
    private val authManager: AuthManager
) : AuthRepository, BaseRepository() {

    override suspend fun checkEmail(email: String): CheckEmail {
        return safeApiCall {
            authService.checkEmail(CheckEmailRequest(email))
        }.toDomain()
    }

    override suspend fun loginOrRegister(request: LoginRequest): UserSession {
        val token =
            authManager.getBearerToken() ?: throw UnauthorizedException("User Token is null")

        return safeApiCall {
            authService.loginOrRegister(token, request)
        }.toDomain()
    }

    override suspend fun sendOtp(request: SendOtpRequest): Boolean {
        return safeApiCall(successCodes = setOf(201)) {
            authService.sendOtp(request)
        }.email.isNotEmpty()
    }

    override suspend fun verifyOtp(request: VerifyOtpRequest): Boolean {
        return safeApiCall {
            authService.verifyOtp(request)
        }.verified
    }

    override suspend fun getAllAliases(): List<Alias> {
        val cached = aliasDao.getAllAliases()

        if (cached.isNotEmpty()) {
            return cached.map { it.toDomain() }
        }

        val token =
            authManager.getBearerToken() ?: throw UnauthorizedException("User Token is null")

        val response = safeApiCall { authService.getAllAliases(token) }

        val updateEntity = response.map { it.toEntity() }

        aliasDao.insertAliases(updateEntity)

        return updateEntity.map { it.toDomain() }
    }

    override suspend fun getAllSchools(): List<School> {
        val cached = schoolDao.getAllSchools()

        if (cached.isNotEmpty()) {
            return cached.map { it.toDomain() }
        }

        val token =
            authManager.getBearerToken() ?: throw UnauthorizedException("User Token is null")

        val response = safeApiCall { authService.getAllSchools(token) }

        val updateEntity = response.map { it.toEntity() }

        schoolDao.insertSchools(updateEntity)

        return updateEntity.map { it.toDomain() }
    }

    override suspend fun resetPassword(request: ResetPasswordRequest) {

        safeApiCallNullable(successCodes = setOf(200)) {
            authService.resetPassword(request)
        }
    }

    override suspend fun updateAlias(request: UpdateAliasRequest): Int {
        val userId =
            authManager.uid ?: throw UnauthorizedException("User Id is null")
        val token =
            authManager.getBearerToken() ?: throw UnauthorizedException("User Token is null")

        val newRequest = request.copy(userId = userId)

        return safeApiCall(successCodes = setOf(200)) {
            authService.updateAlias(token, newRequest)
        }.aliasIndex
    }

    override suspend fun updateSchool(request: UpdateSchoolRequest) {
        val userId =
            authManager.uid ?: throw UnauthorizedException("User Id is null")
        val token =
            authManager.getBearerToken() ?: throw UnauthorizedException("User Token is null")

        val newRequest = request.copy(userId = userId)

        safeApiCallNullable(successCodes = setOf(200)) {
            authService.updateSchool(token, newRequest)
        }
    }

}
