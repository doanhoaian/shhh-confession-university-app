package vn.dihaver.tech.shhh.confession.core.domain.auth.usecase

import vn.dihaver.tech.shhh.confession.core.domain.auth.AuthRepository
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.VerifyOtpRequest
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(request: VerifyOtpRequest): Boolean {
        return authRepo.verifyOtp(request)
    }
}