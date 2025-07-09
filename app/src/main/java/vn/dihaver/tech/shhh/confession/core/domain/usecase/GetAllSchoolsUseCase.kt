package vn.dihaver.tech.shhh.confession.core.domain.usecase

import vn.dihaver.tech.shhh.confession.core.domain.repository.AuthRepository
import vn.dihaver.tech.shhh.confession.core.domain.model.School
import javax.inject.Inject

class GetAllSchoolsUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(): List<School> {
        return authRepo.getAllSchools()
    }
}