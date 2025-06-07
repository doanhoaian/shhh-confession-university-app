package vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateAliasDto(
    @SerializedName("alias_index")
    val aliasIndex: Int
)
