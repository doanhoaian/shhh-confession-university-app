package vn.dihaver.tech.shhh.confession.core.domain.auth.usecase

import vn.dihaver.tech.shhh.confession.core.domain.auth.AuthRepository
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.Alias
import javax.inject.Inject

class GetAllAliasesUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(): List<Alias> {
        return authRepo.getAllAliases()
    }
}