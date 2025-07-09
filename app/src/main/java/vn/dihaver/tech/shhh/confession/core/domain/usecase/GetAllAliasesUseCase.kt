package vn.dihaver.tech.shhh.confession.core.domain.usecase

import vn.dihaver.tech.shhh.confession.core.domain.repository.AuthRepository
import vn.dihaver.tech.shhh.confession.core.domain.model.Alias
import javax.inject.Inject

class GetAllAliasesUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(): List<Alias> {
        return authRepo.getAllAliases()
    }
}