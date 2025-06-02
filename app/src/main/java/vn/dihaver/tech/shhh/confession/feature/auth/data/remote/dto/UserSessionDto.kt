package vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserSessionDto(
    @SerializedName("user_id")
    val uid: String,
    val email: String,
    val status: String,
    @SerializedName("login_method")
    val loginMethod: LoginMethod,
    @SerializedName("alias_id")
    val aliasId: String?,
    @SerializedName("alias_index")
    val aliasIndex: Int?,
    @SerializedName("display_name")
    val displayName: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("school_id")
    val schoolId: Int?,
    @SerializedName("school_name")
    val schoolName: String?,
    @SerializedName("school_short_name")
    val schoolShortName: String?,
    @SerializedName("school_logo_url")
    val schoolLogoUrl: String?,
    @SerializedName("banned_reason")
    val bannedReason: String?,
    @SerializedName("deleted_reason")
    val deletedReason: String?
)