package com.github.edasich.place.finder.data

import androidx.paging.*
import com.github.edasich.location.domain.DeviceLocation
import com.github.edasich.place.finder.data.local.PlaceLocalDataSource
import com.github.edasich.place.finder.data.mapper.PlaceMapper
import com.github.edasich.place.finder.data.paged.NearbyPlaceRemoteMediator
import com.github.edasich.place.finder.data.remote.PlaceRemoteDataSource
import com.github.edasich.place.finder.domain.AllowedDistance
import com.github.edasich.place.finder.domain.Place
import com.github.edasich.place.finder.domain.PlaceId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val localDataSource: PlaceLocalDataSource,
    private val remoteDataSource: PlaceRemoteDataSource,
    private val placeMapper: PlaceMapper
) : PlaceRepository {

    override fun getNearbyPlaces(
        allowedDistance: AllowedDistance,
        currentDeviceLocation: DeviceLocation
    ): Flow<List<Place>> {
        val localQueryParams = placeMapper
            .mapToPlaceToSearchParams(
                allowedDistance = allowedDistance,
                deviceLocation = currentDeviceLocation
            )
        return localDataSource.getPlaces(
            params = localQueryParams
        ).map {
            placeMapper.mapToPlaces(entities = it)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPaginatedNearbyPlaces(
        allowedDistance: AllowedDistance,
        currentDeviceLocation: DeviceLocation
    ): Flow<PagingData<Place>> {
        val localQueryParams = placeMapper
            .mapToPlaceToSearchParams(
                allowedDistance = allowedDistance,
                deviceLocation = currentDeviceLocation
            )
        val remoteQueryLatLng =
            "${currentDeviceLocation.latitude.latitude},${currentDeviceLocation.longitude.longitude}"
        val remoteQueryRadius = allowedDistance.distanceInMeter
        val pagingConfig = PagingConfig(
            pageSize = 5,
            enablePlaceholders = true,
            initialLoadSize = 5,
        )

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { localDataSource.getPaginatedPlaces(params = localQueryParams) },
            remoteMediator = NearbyPlaceRemoteMediator(
                localDataSource = localDataSource,
                remoteDataSource = remoteDataSource,
                mapper = placeMapper,
                queryLatLng = remoteQueryLatLng,
                queryRadius = remoteQueryRadius
            )
        )
            .flow
            .map {
                it.map {
                    placeMapper.mapToPlace(entity = it)
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

    override fun getPaginatedFavoriteNearbyPlaces(): Flow<PagingData<Place>> {
        val pagingConfig = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false
        )
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { localDataSource.getPaginatedFavoritePlaces() }
        )
            .flow
            .map {
                it.map {
                    placeMapper.mapToPlace(entity = it)
                }
            }
    }

}