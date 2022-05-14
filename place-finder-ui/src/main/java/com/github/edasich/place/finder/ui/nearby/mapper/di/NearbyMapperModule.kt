package com.github.edasich.place.finder.ui.nearby.mapper.di

import com.github.edasich.place.finder.ui.nearby.mapper.NearbyPlaceMapper
import com.github.edasich.place.finder.ui.nearby.mapper.internal.NearbyPlaceMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class NearbyMapperModule {

    @Singleton
    @Binds
    abstract fun bindNearbyPlaceMapper(
        impl : NearbyPlaceMapperImpl
    ) : NearbyPlaceMapper

}