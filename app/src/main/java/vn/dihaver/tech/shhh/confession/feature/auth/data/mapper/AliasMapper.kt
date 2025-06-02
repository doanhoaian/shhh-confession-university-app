package vn.dihaver.tech.shhh.confession.feature.auth.data.mapper

import vn.dihaver.tech.shhh.confession.core.domain.auth.model.Alias
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.entity.AliasEntity
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.AliasDto

fun AliasDto.toEntity() = AliasEntity(
    id = id,
    displayName = displayName,
    imageUrl = imageUrl,
    lastedUpdate = System.currentTimeMillis()
)

fun AliasEntity.toDomain() = Alias(
    id = id,
    displayName = displayName,
    imageUrl = imageUrl
)