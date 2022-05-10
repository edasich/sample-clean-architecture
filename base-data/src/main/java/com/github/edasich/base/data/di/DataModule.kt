package com.github.edasich.base.data.di

import com.github.edasich.base.data.local.RemoteConfigProvider
import com.github.edasich.base.data.local.RemoteConfigProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindRemoteConfigProvider(
        impl: RemoteConfigProviderImpl
    ): RemoteConfigProvider

}