package vn.dihaver.tech.shhh.confession.feature.auth.data.mapper

import vn.dihaver.tech.shhh.confession.core.domain.auth.model.UserSession
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.UserSessionDto

fun UserSessionDto.toDomain() = UserSession(
    uid = uid,
    email = email,
    status = status,
    loginMethod = loginMethod,
    aliasId = aliasId,
    aliasIndex = aliasIndex,
    displayName = displayName,
    avatarUrl = avatarUrl,
    schoolId = schoolId,
    schoolName = schoolName,
    schoolShortName = schoolShortName,
    schoolLogoUrl = schoolLogoUrl,
    bannedReason = bannedReason,
    deletedReason = deletedReason
)