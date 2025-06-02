package vn.dihaver.tech.shhh.confession.feature.auth.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("alias")
data class AliasEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val imageUrl: String,

    val lastedUpdate: Long
)
