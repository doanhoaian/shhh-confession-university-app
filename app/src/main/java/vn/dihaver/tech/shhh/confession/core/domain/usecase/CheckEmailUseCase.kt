package vn.dihaver.tech.shhh.confession.core.domain.usecase

import vn.dihaver.tech.shhh.confession.core.domain.repository.AuthRepository
import vn.dihaver.tech.shhh.confession.feature.auth.model.CheckEmail
import javax.inject.Inject

class CheckEmailUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(email: String): CheckEmail {
        return authRepo.checkEmail(email)
    }
}