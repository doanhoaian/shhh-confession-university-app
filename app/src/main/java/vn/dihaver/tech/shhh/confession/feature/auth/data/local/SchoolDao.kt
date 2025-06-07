package vn.dihaver.tech.shhh.confession.feature.auth.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.entity.SchoolEntity

@Dao
interface SchoolDao {
    @Query("SELECT * FROM schools")
    suspend fun getAllSchools(): List<SchoolEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchools(schools: List<SchoolEntity>)
}