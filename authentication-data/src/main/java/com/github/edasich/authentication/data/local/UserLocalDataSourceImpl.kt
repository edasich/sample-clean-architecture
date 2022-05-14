package com.github.edasich.authentication.data.local

import com.github.edasich.authentication.data.BuildConfig
import com.github.edasich.authentication.data.local.model.UserSessionEntity
import java.time.LocalDateTime
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor() : UserLocalDataSource {

    override fun getUserSession(): UserSessionEntity {
        return UserSessionEntity(
            isAuthorized = true,
            userAuthToken = BuildConfig.PLACE_API_KEY,
            userAuthRefreshToken = "",
            userAuthTokenExpireTime = LocalDateTime.now().plusYears(10)
        )
    }

}