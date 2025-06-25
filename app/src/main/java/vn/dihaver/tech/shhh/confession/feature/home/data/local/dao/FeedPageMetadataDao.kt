package vn.dihaver.tech.shhh.confession.feature.home.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.FeedPageMetadataEntity

@Dao
interface FeedPageMetadataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(metadata: FeedPageMetadataEntity)

    @Query("SELECT * FROM feed_page_metadata WHERE feedIdentifier = :feedIdentifier")
    suspend fun getMetadataForFeed(feedIdentifier: String): FeedPageMetadataEntity?

    @Query("DELETE FROM feed_page_metadata WHERE feedIdentifier = :feedIdentifier")
    suspend fun clearMetadata(feedIdentifier: String)
}