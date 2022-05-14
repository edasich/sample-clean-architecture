package com.github.edasich.place.finder.data.remote

import arrow.core.Either
import com.github.edasich.base.data.remote.rest.EitherApiExecutor
import com.github.edasich.base.data.remote.rest.RemoteError
import com.github.edasich.place.finder.data.remote.rest.PlaceRestService
import com.github.edasich.place.finder.data.remote.rest.model.NearbyPlacesApiRequest
import com.github.edasich.place.finder.data.remote.rest.model.NearbyPlacesApiResponse
import com.github.edasich.place.finder.data.remote.rest.model.NextNearbyPlacesApiRequest
import retrofit2.Response
import javax.inject.Inject

class PlaceRemoteDataSourceImpl @Inject constructor(
    private val apiExecutor: EitherApiExecutor,
    private val restService: PlaceRestService
) : PlaceRemoteDataSource {

    override suspend fun fetchNearbyPlaces(
        apiRequest: NearbyPlacesApiRequest
    ): Either<RemoteError, NearbyPlacesApiResponse> =
        apiExecutor
            .executeWithResponse {
                restService.fetchNearbyPlaces(
                    latLng = apiRequest.latLng,
                    radius = apiRequest.radius
                )
            }
            .map {
                extractNearbyPlacesFromResponseThenApplyNextLinkIfExist(response = it)
            }

    override suspend fun fetchNextNearbyPlaces(
        apiRequest: NextNearbyPlacesApiRequest
    ): Either<RemoteError, NearbyPlacesApiResponse> =
        apiExecutor
            .executeWithResponse {
                restService.fetchNextNearbyPlaces(
                    nextNearbyPlacesLink = apiRequest.nextNearbyPlacesLink,
                )
            }
            .map {
                extractNearbyPlacesFromResponseThenApplyNextLinkIfExist(response = it)
            }

    private fun extractNearbyPlacesFromResponseThenApplyNextLinkIfExist(
        response: Response<NearbyPlacesApiResponse>
    ): NearbyPlacesApiResponse {
        return response.body()!!.apply {
            val nextLink = response.headers()["link"]
            nextNearbyPlacesLink = nextLink
        }
    }

}