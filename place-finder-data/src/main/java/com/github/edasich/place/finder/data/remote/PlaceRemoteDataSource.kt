package com.github.edasich.place.finder.data.remote

import arrow.core.Either
import com.github.edasich.base.data.remote.rest.RemoteError
import com.github.edasich.place.finder.data.remote.rest.model.NearbyPlacesApiRequest
import com.github.edasich.place.finder.data.remote.rest.model.NearbyPlacesApiResponse
import com.github.edasich.place.finder.data.remote.rest.model.NextNearbyPlacesApiRequest

interface PlaceRemoteDataSource {

    suspend fun fetchNearbyPlaces(
        apiRequest: NearbyPlacesApiRequest
    ): Either<RemoteError, NearbyPlacesApiResponse>

    suspend fun fetchNextNearbyPlaces(
        apiRequest: NextNearbyPlacesApiRequest
    ): Either<RemoteError, NearbyPlacesApiResponse>

}