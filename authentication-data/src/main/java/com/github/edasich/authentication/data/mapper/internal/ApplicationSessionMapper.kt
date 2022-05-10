package com.github.edasich.authentication.data.mapper.internal

import com.github.edasich.authentication.data.local.model.UserSessionEntity
import com.github.edasich.base.data.local.model.ApplicationSession

interface ApplicationSessionMapper {

    fun mapToApplicationSession(
        entity: UserSessionEntity
    ): ApplicationSession

}