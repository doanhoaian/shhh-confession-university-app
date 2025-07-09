package vn.dihaver.tech.shhh.confession.feature.post.model

import com.google.gson.annotations.SerializedName

enum class PostType {
    @SerializedName("confession")
    CONFESSION,
    @SerializedName("unknown")
    UNKNOWN;

    companion object {
        fun fromString(type: String?): PostType {
            return when (type?.uppercase()) {
                "CONFESSION" -> CONFESSION
                else -> UNKNOWN
            }
        }
    }
}