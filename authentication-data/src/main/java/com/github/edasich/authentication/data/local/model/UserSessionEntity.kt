package com.github.edasich.authentication.data.local.model

import java.time.LocalDateTime

data class UserSessionEntity(
    val isAuthorized: Boolean,
    val userAuthToken: String?,
    val userAuthRefreshToken: String,
    val userAuthTokenExpireTime: LocalDateTime
)
