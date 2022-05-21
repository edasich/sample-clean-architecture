package com.github.edasich.place.finder.data.paged

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.github.edasich.place.finder.data.local.PlaceLocalDataSource
import com.github.edasich.place.finder.data.local.model.PlaceEntity
import com.github.edasich.place.finder.data.local.model.RemoteKey
import com.github.edasich.place.finder.data.mapper.PlaceMapper
import com.github.edasich.place.finder.data.remote.PlaceRemoteDataSource
import com.github.edasich.place.finder.data.remote.rest.model.NearbyPlacesApiRequest
import com.github.edasich.place.finder.data.remote.rest.model.NextNearbyPlacesApiRequest

@OptIn(ExperimentalPagingApi::class)
class NearbyPlaceRemoteMediator(
    private val localDataSource: PlaceLocalDataSource,
    private val remoteDataSource: PlaceRemoteDataSource,
    private val mapper: PlaceMapper,
    private val queryLatLng: String,
    private val queryRadius: Int,
) : RemoteMediator<Int, PlaceEntity>() {

    val TAG = "mediator"

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PlaceEntity>
    ): MediatorResult {
        val nextLink: String? = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = localDataSource.getRemoteKey(query = queryLatLng)
                if (remoteKey.nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKey.nextKey
            }
        }

        val response = if (nextLink == null) {
            val apiRequest = NearbyPlacesApiRequest(
                latLng = queryLatLng,
                radius = queryRadius
            )
            remoteDataSource.fetchNearbyPlaces(apiRequest)
        } else {
            val apiRequest = NextNearbyPlacesApiRequest(
                nextNearbyPlacesLink = nextLink
            )
            remoteDataSource.fetchNextNearbyPlaces(apiRequest)
        }

        return response.fold(
            ifLeft = {
                MediatorResult.Error(IllegalArgumentException())
            },
            ifRight = {
                if (loadType == LoadType.REFRESH) {
                    localDataSource.deleteRemoteKey(queryLatLng)
                }

                localDataSource.saveRemoteKey(
                    remoteKey = RemoteKey(
                        latLng = queryLatLng,
                        nextKey = it.nextNearbyPlacesLink
                    )
                )

                localDataSource.savePlaces(places = mapper.mapToPlaceEntities(apiResponse = it))

                MediatorResult.Success(endOfPaginationReached = it.nextNearbyPlacesLink == null)
            }
        )

    }

}