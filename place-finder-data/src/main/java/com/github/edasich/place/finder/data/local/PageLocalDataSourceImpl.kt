package com.github.edasich.place.finder.data.local

import com.github.edasich.place.finder.data.local.dao.PlaceDao
import com.github.edasich.place.finder.data.local.model.PlaceEntity
import com.github.edasich.place.finder.data.local.model.PlaceEntityWithoutFavored
import com.github.edasich.place.finder.data.local.model.PlaceToSearchParams
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class PageLocalDataSourceImpl @Inject constructor(
    private val placeDao: PlaceDao
) : PageLocalDataSource {

    private val currentNextLink: AtomicReference<String?> = AtomicReference()

    override fun getPlaces(
        params: PlaceToSearchParams
    ): Flow<List<PlaceEntity>> {
        return placeDao.getPlaces(
            pointSouthX = params.pointSouthX,
            pointNorthX = params.pointNorthX,
            pointEastY = params.pointEastY,
            pointWestY = params.pointWestY,
            limit = 10,
            page = 1
        )
    }

    override suspend fun getPlace(placeId: String) : PlaceEntity? {
        return placeDao.getPlace(placeId = placeId)
    }

    override suspend fun savePlaces(places: List<PlaceEntityWithoutFavored>) {
        placeDao.upsertPlacesWithoutFavored(entities = places)
    }

    override suspend fun updatePlace(place: PlaceEntity) {
        placeDao.upsert(entity = place)
    }

    override fun getFavoritePlaces(): Flow<List<PlaceEntity>> {
        return placeDao.getFavoritePlaces()
    }

    override suspend fun saveNextNearbyPlacesLink(nextLink: String) {
        currentNextLink.set(nextLink)
    }

    override suspend fun getNextNearbyPlacesLink(): String? {
        return currentNextLink.get()
    }

    override suspend fun getAndDeleteNextNearbyPlacesLink(): String? {
        val nextLink = getNextNearbyPlacesLink()
        deleteNextNearbyPlacesLink()
        return nextLink
    }

    override suspend fun deleteNextNearbyPlacesLink() {
        currentNextLink.set(null)
    }

}