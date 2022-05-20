package com.github.edasich.place.finder.data.local

import androidx.paging.PagingSource
import com.github.edasich.place.finder.data.local.dao.PlaceDao
import com.github.edasich.place.finder.data.local.dao.RemoteKeyDao
import com.github.edasich.place.finder.data.local.model.PlaceEntity
import com.github.edasich.place.finder.data.local.model.PlaceEntityWithoutFavored
import com.github.edasich.place.finder.data.local.model.PlaceToSearchParams
import com.github.edasich.place.finder.data.local.model.RemoteKey
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaceLocalDataSourceImpl @Inject constructor(
    private val placeDao: PlaceDao,
    private val remoteKeyDao: RemoteKeyDao
) : PlaceLocalDataSource {

    override fun getPlaces(
        params: PlaceToSearchParams
    ): Flow<List<PlaceEntity>> {
        return placeDao.getPlaces(
            pointSouthX = params.pointSouthX,
            pointNorthX = params.pointNorthX,
            pointEastY = params.pointEastY,
            pointWestY = params.pointWestY,
        )
    }

    override fun getPaginatedPlaces(
        params: PlaceToSearchParams
    ): PagingSource<Int, PlaceEntity> {
        return placeDao.getPaginatedPlaces(
            pointSouthX = params.pointSouthX,
            pointNorthX = params.pointNorthX,
            pointEastY = params.pointEastY,
            pointWestY = params.pointWestY,
        )
    }

    override suspend fun getPlace(placeId: String): PlaceEntity? {
        return placeDao.getPlace(placeId = placeId)
    }

    override suspend fun savePlaces(places: List<PlaceEntityWithoutFavored>) {
        placeDao.upsertPlacesWithoutFavored(entities = places)
    }

    override suspend fun updatePlace(place: PlaceEntity) {
        placeDao.upsert(entity = place)
    }

    override fun getPaginatedFavoritePlaces(): PagingSource<Int, PlaceEntity> {
        return placeDao.getFavoritePlaces()
    }

    override suspend fun saveRemoteKey(remoteKey: RemoteKey) {
        remoteKeyDao.insertOrReplace(remoteKey)
    }

    override suspend fun getRemoteKey(query: String): RemoteKey {
        return remoteKeyDao.remoteKeyByQuery(query)
    }

    override suspend fun deleteRemoteKey(query: String) {
        remoteKeyDao.deleteByQuery(query)
    }

}