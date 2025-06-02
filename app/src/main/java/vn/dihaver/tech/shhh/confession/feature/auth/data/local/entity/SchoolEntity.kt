package vn.dihaver.tech.shhh.confession.feature.auth.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("schools")
data class SchoolEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val shortName: String,
    val imageUrl: String,

    val lastedUpdate: Long
)
