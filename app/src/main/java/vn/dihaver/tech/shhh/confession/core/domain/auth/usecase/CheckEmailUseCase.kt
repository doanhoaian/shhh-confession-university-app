package vn.dihaver.tech.shhh.confession.core.domain.auth.usecase

import vn.dihaver.tech.shhh.confession.core.domain.auth.AuthRepository
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.CheckEmail
import javax.inject.Inject

class CheckEmailUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(email: String): CheckEmail {
        return authRepo.checkEmail(email)
    }
}