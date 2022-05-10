package com.github.edasich.base.data.local

import com.github.edasich.base.data.local.model.ServerConfig

interface RemoteConfigProvider {
    fun provideRestConfig(): ServerConfig
}