package vn.dihaver.tech.shhh.confession.feature.home.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import vn.dihaver.tech.shhh.confession.core.util.ListStringConverter

@Entity(tableName = "posts")
@TypeConverters(ListStringConverter::class)
data class PostEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val schoolId: Int,
    val postType: String,
    val avatarUrl: String,
    val displayName: String,
    val schoolShortName: String,
    val content: String,
    val images: List<String>,
    val status: String,
    val commentPermission: String,
    val viewPermission: String,
    val totalLike: Long,
    val totalDislike: Long,
    val totalComment: Long,
    val createdAt: String,
    val updatedAt: String,

    val lastUpdated: Long
)
