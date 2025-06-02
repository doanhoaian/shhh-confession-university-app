package vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateSchoolRequest(
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("school_id")
    val schoolId: Int
)
