package vn.dihaver.tech.shhh.confession.core.domain.model

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
) {
    val isGuest: Boolean
        get() = this.uid == GUEST.uid

    companion object {
        val GUEST = UserSession(
            uid = "guest_session_id",
            email = "",
            status = "",
            loginMethod = LoginMethod.None,
            aliasId = null,
            aliasIndex = null,
            displayName = null,
            avatarUrl = null,
            schoolId = null,
            schoolName = null,
            schoolShortName = null,
            schoolLogoUrl = null,
            bannedReason = null,
            deletedReason = null
        )
    }
}
