package com.github.edasich.place.finder.data.mapper

import com.github.edasich.location.domain.DeviceLocation
import com.github.edasich.place.finder.data.local.model.PlaceEntity
import com.github.edasich.place.finder.data.local.model.PlaceEntityWithoutFavored
import com.github.edasich.place.finder.data.local.model.PlaceToSearchParams
import com.github.edasich.place.finder.data.remote.rest.model.NearbyPlacesApiRequest
import com.github.edasich.place.finder.data.remote.rest.model.NearbyPlacesApiResponse
import com.github.edasich.place.finder.domain.AllowedDistance
import com.github.edasich.place.finder.domain.Place

interface PlaceMapper {

    suspend fun mapToPlaces(
        entities: List<PlaceEntity>
    ): List<Place>

    suspend fun mapToPlaceOrNull(
        entity: PlaceEntity?
    ): Place?

    suspend fun mapToPlaceEntity(
        place: Place
    ): PlaceEntity

    suspend fun mapToPlaceEntities(
        apiResponse: NearbyPlacesApiResponse
    ): List<PlaceEntityWithoutFavored>

    suspend fun mapToPlaceToSearchParams(
        allowedDistance: AllowedDistance,
        deviceLocation: DeviceLocation
    ): PlaceToSearchParams

    suspend fun mapToNearbyPlacesApiRequest(
        allowedDistance: AllowedDistance,
        currentDeviceLocation: DeviceLocation
    ): NearbyPlacesApiRequest

}