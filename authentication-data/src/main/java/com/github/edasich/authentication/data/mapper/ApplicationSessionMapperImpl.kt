package com.github.edasich.authentication.data.mapper

import com.github.edasich.authentication.data.local.model.UserSessionEntity
import com.github.edasich.authentication.data.mapper.internal.ApplicationSessionMapper
import com.github.edasich.base.data.local.model.AccessToken
import com.github.edasich.base.data.local.model.ApplicationSession
import com.github.edasich.base.data.local.model.SessionCredential
import javax.inject.Inject

class ApplicationSessionMapperImpl @Inject constructor() : ApplicationSessionMapper {

    override fun mapToApplicationSession(
        entity: UserSessionEntity
    ): ApplicationSession {
        return if (entity.isAuthorized && entity.userAuthToken != null) {
            ApplicationSession.Authorized(
                sessionCredential = SessionCredential(
                    restAccessToken = AccessToken(
                        token = entity.userAuthToken,
                        refreshToken = entity.userAuthRefreshToken,
                        expireTime = entity.userAuthTokenExpireTime
                    ),
                )
            )
        } else {
            ApplicationSession.Unauthorized
        }
    }

}