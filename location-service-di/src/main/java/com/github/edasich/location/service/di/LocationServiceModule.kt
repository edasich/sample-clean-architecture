package com.github.edasich.location.service.di

import com.github.edasich.location.service.GetLocationCollector
import com.github.edasich.location.service.StartLocationCollector
import com.github.edasich.location.service.StopLocationCollector
import com.github.edasich.location.service.internal.GetLocationCollectorImpl
import com.github.edasich.location.service.internal.StartLocationCollectorImpl
import com.github.edasich.location.service.internal.StopLocationCollectorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class LocationServiceModule {

    @Binds
    abstract fun bindGetLocationCollector(
        impl: GetLocationCollectorImpl
    ): GetLocationCollector

    @Binds
    abstract fun bindStartLocationCollector(
        impl: StartLocationCollectorImpl
    ): StartLocationCollector


    @Binds
    abstract fun bindStopLocationCollector(
        impl: StopLocationCollectorImpl
    ): StopLocationCollector
}