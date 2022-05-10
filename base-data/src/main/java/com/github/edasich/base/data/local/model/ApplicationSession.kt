package com.github.edasich.base.data.local.model

sealed class ApplicationSession {
    data class Authorized(
        val sessionCredential: SessionCredential
    ) : ApplicationSession()

    object Unauthorized : ApplicationSession()
}