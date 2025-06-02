package vn.dihaver.tech.shhh.confession.core.domain.auth.model

import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.LoginMethod

data class UserSession(
    val uid: String,
    val email: String,
    val status: String,
    val loginMethod: LoginMethod,
    val aliasId: String?,
    val aliasIndex: Int?,
    val displayName: String?,
    val avatarUrl: String?,
    val schoolId: Int?,
    val schoolName: String?,
    val schoolShortName: String?,
    val schoolLogoUrl: String?,
    val bannedReason: String?,
    val deletedReason: String?
)
