package vn.dihaver.tech.shhh.confession.feature.home.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_page_metadata")
data class FeedPageMetadataEntity(
    @PrimaryKey
    val feedIdentifier: String,

    val nextPostId: String?,
    val nextScore: Double?
)