package com.github.edasich.authentication.data.di

import com.github.edasich.authentication.data.local.ApplicationSessionProviderImpl
import com.github.edasich.authentication.data.local.UserLocalDataSource
import com.github.edasich.authentication.data.local.UserLocalDataSourceImpl
import com.github.edasich.authentication.data.mapper.ApplicationSessionMapperImpl
import com.github.edasich.authentication.data.mapper.internal.ApplicationSessionMapper
import com.github.edasich.base.data.local.ApplicationSessionProvider
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
    abstract fun bindUserLocalDataSource(
        impl: UserLocalDataSourceImpl,
    ): UserLocalDataSource

    @Singleton
    @Binds
    abstract fun bindApplicationSessionProvider(
        impl: ApplicationSessionProviderImpl
    ): ApplicationSessionProvider

    @Binds
    abstract fun bindApplicationSessionMapper(
        impl: ApplicationSessionMapperImpl
    ): ApplicationSessionMapper

}