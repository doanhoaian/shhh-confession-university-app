package vn.dihaver.tech.shhh.confession.core.domain.usecase

import vn.dihaver.tech.shhh.confession.core.domain.repository.AuthRepository
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.ResetPasswordRequest
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(request: ResetPasswordRequest) {
        return authRepo.resetPassword(request)
    }
}