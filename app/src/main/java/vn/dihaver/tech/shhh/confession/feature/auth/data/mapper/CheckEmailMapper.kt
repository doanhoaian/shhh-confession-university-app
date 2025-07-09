package vn.dihaver.tech.shhh.confession.feature.auth.data.mapper

import vn.dihaver.tech.shhh.confession.feature.auth.model.CheckEmail
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.CheckEmailDto

fun CheckEmailDto.toDomain() = CheckEmail(
    isExists = isExists,
    providers = providers
)