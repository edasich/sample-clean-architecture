package com.github.edasich.authentication.data.local

import com.github.edasich.authentication.data.local.model.UserSessionEntity

interface UserLocalDataSource {
    fun getUserSession(): UserSessionEntity
}