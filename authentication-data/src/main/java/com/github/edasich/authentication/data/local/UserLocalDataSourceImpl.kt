package com.github.edasich.authentication.data.local

import com.github.edasich.authentication.data.local.model.UserSessionEntity
import java.time.LocalDateTime
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor() : UserLocalDataSource {

    override fun getUserSession(): UserSessionEntity {
        return UserSessionEntity(
            isAuthorized = true,
            userAuthToken = "fsq3NJpS3tyk0gzzIgD43W1i++/ejP8c259xZ+nhuvdfqSU=",
            userAuthRefreshToken = "",
            userAuthTokenExpireTime = LocalDateTime.now().plusYears(10)
        )
    }

}