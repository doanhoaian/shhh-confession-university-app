package vn.dihaver.tech.shhh.confession.core.domain.auth.usecase

import vn.dihaver.tech.shhh.confession.core.domain.auth.AuthRepository
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.UpdateSchoolRequest
import javax.inject.Inject

class UpdateSchoolUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(request: UpdateSchoolRequest) {
        return authRepo.updateSchool(request)
    }
}