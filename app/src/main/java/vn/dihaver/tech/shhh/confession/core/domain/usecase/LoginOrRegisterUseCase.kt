package vn.dihaver.tech.shhh.confession.core.domain.usecase

import vn.dihaver.tech.shhh.confession.core.domain.repository.AuthRepository
import vn.dihaver.tech.shhh.confession.core.domain.model.UserSession
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.LoginRequest
import javax.inject.Inject

class LoginOrRegisterUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(request: LoginRequest): UserSession {
        return authRepo.loginOrRegister(request)
    }
}