package vn.dihaver.tech.shhh.confession.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.AliasDao
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.SchoolDao
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.entity.AliasEntity
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.entity.SchoolEntity

@Database(
    entities = [AliasEntity::class, SchoolEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun aliasDao(): AliasDao
    abstract fun schoolDao(): SchoolDao
}