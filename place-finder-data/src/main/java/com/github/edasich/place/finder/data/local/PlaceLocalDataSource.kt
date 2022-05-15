package com.github.edasich.place.finder.data.local

import com.github.edasich.place.finder.data.local.model.PlaceEntity
import com.github.edasich.place.finder.data.local.model.PlaceEntityWithoutFavored
import com.github.edasich.place.finder.data.local.model.PlaceToSearchParams
import kotlinx.coroutines.flow.Flow

interface PlaceLocalDataSource {
    fun getPlaces(params: PlaceToSearchParams): Flow<List<PlaceEntity>>
    suspend fun getPlace(placeId: String): PlaceEntity?
    suspend fun savePlaces(places: List<PlaceEntityWithoutFavored>)
    suspend fun updatePlace(place: PlaceEntity)

    fun getFavoritePlaces(): Flow<List<PlaceEntity>>

    suspend fun saveNextNearbyPlacesLink(nextLink: String)
    suspend fun getNextNearbyPlacesLink(): String?
    suspend fun getAndDeleteNextNearbyPlacesLink(): String?
    suspend fun deleteNextNearbyPlacesLink()
}