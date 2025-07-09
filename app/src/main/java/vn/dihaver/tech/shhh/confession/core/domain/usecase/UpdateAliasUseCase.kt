package vn.dihaver.tech.shhh.confession.core.domain.usecase

import vn.dihaver.tech.shhh.confession.core.domain.repository.AuthRepository
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.UpdateAliasRequest
import javax.inject.Inject

class UpdateAliasUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(request: UpdateAliasRequest): Int {
        return authRepo.updateAlias(request)
    }
}