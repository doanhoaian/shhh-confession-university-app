package vn.dihaver.tech.shhh.confession.feature.home.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_remote_keys")
data class FeedRemoteKeyEntity(
    @PrimaryKey val postId: String,
    val cachedForUserId: String?,
    val version: String,
    val orderInFeed: Int,
    val createdAt: Long
)