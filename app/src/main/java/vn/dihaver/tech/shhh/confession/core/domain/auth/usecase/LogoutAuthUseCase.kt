package vn.dihaver.tech.shhh.confession.core.domain.auth.usecase

import vn.dihaver.tech.shhh.confession.core.domain.auth.AuthRepository
import javax.inject.Inject

class LogoutAuthUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepo.logoutAuth()
    }
}