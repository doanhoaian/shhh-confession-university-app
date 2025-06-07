package vn.dihaver.tech.shhh.confession.feature.auth.data.mapper

import vn.dihaver.tech.shhh.confession.core.domain.auth.model.School
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.entity.SchoolEntity
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.SchoolDto

fun SchoolDto.toEntity() = SchoolEntity(
    id = id,
    name = name,
    shortName = shortName,
    imageUrl = imageUrl,
    lastedUpdate = System.currentTimeMillis()
)

fun SchoolEntity.toDomain() = School(
    id = id,
    name = name,
    shortName = shortName,
    imageUrl = imageUrl
)