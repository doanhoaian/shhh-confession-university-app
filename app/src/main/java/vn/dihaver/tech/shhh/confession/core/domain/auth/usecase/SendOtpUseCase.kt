package vn.dihaver.tech.shhh.confession.core.domain.auth.usecase

import vn.dihaver.tech.shhh.confession.core.domain.auth.AuthRepository
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.SendOtpRequest
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(request: SendOtpRequest): Boolean {
        return authRepo.sendOtp(request)
    }
}