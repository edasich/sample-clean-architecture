package com.github.edasich.base.data.local

import com.github.edasich.base.data.local.model.ApplicationSession

interface ApplicationSessionProvider {
    fun provideSession(): ApplicationSession
}