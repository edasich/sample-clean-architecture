package com.github.edasich.base.data.local.model

import java.time.LocalDateTime

data class AccessToken(
    val token: String,
    val refreshToken: String,
    val expireTime: LocalDateTime,
) {

    fun hasExpired(): Boolean {
        val currentLocalDateTime = LocalDateTime.now()
        if (currentLocalDateTime.isBefore(expireTime) || currentLocalDateTime.isEqual(expireTime)) {
            return true
        }
        return false
    }

}
