package com.github.edasich.place.finder.data.local

import androidx.paging.PagingSource
import com.github.edasich.place.finder.data.local.model.PlaceEntity
import com.github.edasich.place.finder.data.local.model.PlaceEntityWithoutFavored
import com.github.edasich.place.finder.data.local.model.PlaceToSearchParams
import com.github.edasich.place.finder.data.local.model.RemoteKey
import kotlinx.coroutines.flow.Flow

interface PlaceLocalDataSource {
    fun getPlaces(params: PlaceToSearchParams): Flow<List<PlaceEntity>>
    fun getPaginatedPlaces(params: PlaceToSearchParams): PagingSource<Int, PlaceEntity>
    suspend fun getPlace(placeId: String): PlaceEntity?
    suspend fun savePlaces(places: List<PlaceEntityWithoutFavored>)
    suspend fun updatePlace(place: PlaceEntity)

    fun getPaginatedFavoritePlaces(): PagingSource<Int, PlaceEntity>
    suspend fun saveRemoteKey(remoteKey: RemoteKey)
    suspend fun getRemoteKey(query: String): RemoteKey
    suspend fun deleteRemoteKey(query: String)
}