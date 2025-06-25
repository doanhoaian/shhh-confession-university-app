package vn.dihaver.tech.shhh.confession.feature.home.data.local.entity

import androidx.room.Entity

@Entity(tableName = "user_interactions", primaryKeys = ["postId", "userId"])
data class InteractionEntity(
    val postId: String,
    val userId: String,
    val isLiked: Boolean,
    val isDisliked: Boolean
)