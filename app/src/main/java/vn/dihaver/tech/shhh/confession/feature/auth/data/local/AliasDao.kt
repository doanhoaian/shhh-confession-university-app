package vn.dihaver.tech.shhh.confession.feature.auth.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.entity.AliasEntity

@Dao
interface AliasDao {
    @Query("SELECT * FROM alias ORDER BY displayName")
    suspend fun getAllAliases(): List<AliasEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAliases(aliases: List<AliasEntity>)
}