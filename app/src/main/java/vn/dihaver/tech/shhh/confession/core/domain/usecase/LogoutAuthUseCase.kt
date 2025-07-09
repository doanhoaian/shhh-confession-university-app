package vn.dihaver.tech.shhh.confession.core.domain.usecase

import vn.dihaver.tech.shhh.confession.core.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutAuthUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepo.logoutAuth()
    }
}