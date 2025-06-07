package vn.dihaver.tech.shhh.confession.core.domain.auth.usecase

import vn.dihaver.tech.shhh.confession.core.domain.auth.AuthRepository
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.School
import javax.inject.Inject

class GetAllSchoolsUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(): List<School> {
        return authRepo.getAllSchools()
    }
}