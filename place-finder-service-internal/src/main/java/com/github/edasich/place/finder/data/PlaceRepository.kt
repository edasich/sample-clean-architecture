package com.github.edasich.place.finder.data

import androidx.paging.PagingData
import com.github.edasich.base.service.PagedList
import com.github.edasich.location.domain.DeviceLocation
import com.github.edasich.place.finder.domain.AllowedDistance
import com.github.edasich.place.finder.domain.Place
import com.github.edasich.place.finder.domain.PlaceId
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {

    fun getNearbyPlaces(
        allowedDistance: AllowedDistance,
        currentDeviceLocation: DeviceLocation
    ): Flow<List<Place>>

    fun getPaginatedNearbyPlaces(
        allowedDistance: AllowedDistance,
        currentDeviceLocation: DeviceLocation
    ): Flow<PagingData<Place>>

    suspend fun getNearbyPlaceById(placeId: PlaceId): Place?

    suspend fun updatePlace(place: Place)

    fun getPaginatedFavoriteNearbyPlaces(): Flow<PagingData<Place>>

}