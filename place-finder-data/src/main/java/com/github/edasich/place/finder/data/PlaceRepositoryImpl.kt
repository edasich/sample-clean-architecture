package com.github.edasich.place.finder.data

import com.github.edasich.base.service.PagedList
import com.github.edasich.base.service.toFreshLoadedList
import com.github.edasich.location.domain.DeviceLocation
import com.github.edasich.place.finder.data.local.PlaceLocalDataSource
import com.github.edasich.place.finder.data.mapper.PlaceMapper
import com.github.edasich.place.finder.data.remote.PlaceRemoteDataSource
import com.github.edasich.place.finder.domain.AllowedDistance
import com.github.edasich.place.finder.domain.Place
import com.github.edasich.place.finder.domain.PlaceId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val localDataSource: PlaceLocalDataSource,
    private val remoteDataSource: PlaceRemoteDataSource,
    private val placeMapper: PlaceMapper
) : PlaceRepository {

    override suspend fun getNearbyPlaces(
        allowedDistance: AllowedDistance,
        currentDeviceLocation: DeviceLocation
    ): Flow<PagedList<Place>> {
        val searchParams = placeMapper
            .mapToPlaceToSearchParams(
                allowedDistance = allowedDistance,
                deviceLocation = currentDeviceLocation
            )
        return localDataSource
            .getPlaces(
                params = searchParams
            )
            .map {
                placeMapper.mapToPlaces(entities = it)
            }
            .map {
                it.toFreshLoadedList()
            }
            .onEach {
                if (it.items.isEmpty()) {
                    freshFetch(
                        allowedDistance = allowedDistance,
                        currentDeviceLocation = currentDeviceLocation
                    )
                }
            }
    }

    override suspend fun getNearbyPlaceById(placeId: PlaceId): Place? {
        return localDataSource
            .getPlace(placeId = placeId.id)
            .let {
                placeMapper.mapToPlaceOrNull(entity = it)
            }
    }

    override suspend fun updatePlace(place: Place) {
        placeMapper
            .mapToPlaceEntity(place = place)
            .also {
                localDataSource.updatePlace(place = it)
            }
    }

    override fun getFavoriteNearbyPlaces(): Flow<List<Place>> {
        return localDataSource
            .getFavoritePlaces()
            .map {
                placeMapper.mapToPlaces(entities = it)
            }
    }

    private suspend fun freshFetch(
        allowedDistance: AllowedDistance,
        currentDeviceLocation: DeviceLocation
    ) {
        remoteDataSource
            .fetchNearbyPlaces(
                apiRequest = placeMapper.mapToNearbyPlacesApiRequest(
                    allowedDistance = allowedDistance,
                    currentDeviceLocation = currentDeviceLocation
                )
            )
            .map {
                placeMapper.mapToPlaceEntities(apiResponse = it)
            }
            .tap {
                localDataSource.savePlaces(places = it)
            }
    }

}