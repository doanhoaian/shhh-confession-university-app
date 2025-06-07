package vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateAliasRequest(
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("alias_id")
    val aliasId: String
)
