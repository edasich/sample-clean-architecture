package com.github.edasich.app.place.finder.service.di

import com.github.edasich.palce.finder.service.*
import com.github.edasich.place.finder.service.internal.*
import com.github.edasich.place.finder.service.internal.core.CoreSearchNearbyPlace
import com.github.edasich.place.finder.service.internal.core.CoreSearchNearbyPlaceStatus
import com.github.edasich.place.finder.service.internal.core.StartCoreSearchNearbyPlaceOperations
import com.github.edasich.place.finder.service.internal.core.StopCoreSearchNearbyPlaceOperations
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class PlaceFinderServiceModule {

    @Binds
    abstract fun bindGetNearbyPlaces(
        impl: GetNearbyPlacesImpl
    ): GetNearbyPlaces

    @Binds
    abstract fun bindGetAllowedPlaceDistance(
        impl: GetAllowedPlaceDistanceImpl
    ): GetAllowedPlaceDistance

    @Binds
    abstract fun bindGetSearchingNearbyPlaceStatus(
        impl: GetSearchingNearbyPlaceStatusImpl
    ): GetSearchingNearbyPlaceStatus

    @Binds
    abstract fun bindStartSearchingNearbyPlace(
        impl: StartSearchingNearbyPlaceImpl
    ): StartSearchingNearbyPlace

    @Binds
    abstract fun bindStopSearchingNearbyPlace(
        impl: StopSearchingNearbyPlaceImpl
    ): StopSearchingNearbyPlace

    @Binds
    abstract fun bindAddPlaceToFavorites(
        impl: AddPlaceToFavoritesImpl
    ): AddPlaceToFavorites

    @Binds
    abstract fun bindRemovePlaceFromFavoritesImpl(
        impl: RemovePlaceFromFavoritesImpl
    ): RemovePlaceFromFavorites

    @Binds
    abstract fun bindGetFavoriteNearbyPlaces(
        impl: GetFavoriteNearbyPlacesImpl
    ): GetFavoriteNearbyPlaces

    @Binds
    abstract fun bindCoreSearchNearbyPlaceStatus(
        impl: CoreSearchNearbyPlace
    ): CoreSearchNearbyPlaceStatus

    @Binds
    abstract fun bindStartCoreSearchNearbyPlaceOperations(
        impl: CoreSearchNearbyPlace
    ): StartCoreSearchNearbyPlaceOperations

    @Binds
    abstract fun bindStopCoreSearchNearbyPlaceOperations(
        impl: CoreSearchNearbyPlace
    ): StopCoreSearchNearbyPlaceOperations

}