package vn.dihaver.tech.shhh.confession.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.AliasDao
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.SchoolDao
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.entity.AliasEntity
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.entity.SchoolEntity
import vn.dihaver.tech.shhh.confession.feature.home.data.local.dao.FeedPageMetadataDao
import vn.dihaver.tech.shhh.confession.feature.home.data.local.dao.FeedRemoteKeyDao
import vn.dihaver.tech.shhh.confession.feature.home.data.local.dao.PostDao
import vn.dihaver.tech.shhh.confession.feature.home.data.local.dao.InteractionDao
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.FeedPageMetadataEntity
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.FeedRemoteKeyEntity
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.InteractionEntity
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.PostEntity

@Database(
    entities = [
        AliasEntity::class,
        SchoolEntity::class,
        PostEntity::class,
        FeedRemoteKeyEntity::class,
        FeedPageMetadataEntity::class,
        InteractionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun aliasDao(): AliasDao
    abstract fun schoolDao(): SchoolDao

    abstract fun feedPageMetadataDao(): FeedPageMetadataDao
    abstract fun feedRemoteKeyDao(): FeedRemoteKeyDao
    abstract fun postDao(): PostDao
    abstract fun interactionDao(): InteractionDao
}