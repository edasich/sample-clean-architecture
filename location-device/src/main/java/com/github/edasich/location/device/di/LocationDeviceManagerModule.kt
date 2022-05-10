package com.github.edasich.location.device.di

import android.content.Context
import com.github.edasich.location.device.LocationDeviceManager
import com.github.edasich.location.device.LocationDeviceManagerImpl
import com.github.edasich.location.device.mapper.LocationDeviceMapper
import com.github.edasich.location.device.mapper.internal.LocationDeviceMapperImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
@Module
abstract class LocationDeviceManagerModule {

    companion object {

        @Provides
        fun provideFusedLocationProviderClient(
            @ApplicationContext
            context: Context
        ): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(context)
        }

    }

    @Singleton
    @Binds
    abstract fun bindLocationDeviceManager(
        impl: LocationDeviceManagerImpl,
    ): LocationDeviceManager

    @Singleton
    @Binds
    abstract fun bindLocationDeviceMapper(
        impl: LocationDeviceMapperImpl,
    ): LocationDeviceMapper

}