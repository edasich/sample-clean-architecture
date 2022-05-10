package com.github.edasich.base.data.local

import com.github.edasich.base.data.BuildConfig
import com.github.edasich.base.data.local.model.ServerConfig
import java.net.URI
import javax.inject.Inject

class RemoteConfigProviderImpl @Inject constructor() : RemoteConfigProvider {

    override fun provideRestConfig(): ServerConfig {
        return ServerConfig(
            serverUri = URI(BuildConfig.SERVER_URL)
        )
    }

}