package vn.dihaver.tech.shhh.confession.core.domain.usecase

import vn.dihaver.tech.shhh.confession.core.domain.repository.AuthRepository
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.SendOtpRequest
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(request: SendOtpRequest): Boolean {
        return authRepo.sendOtp(request)
    }
}