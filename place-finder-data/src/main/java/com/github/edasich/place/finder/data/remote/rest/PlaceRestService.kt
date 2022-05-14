package com.github.edasich.place.finder.data.remote.rest

import com.github.edasich.place.finder.data.remote.rest.model.NearbyPlacesApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PlaceRestService {

    companion object {
        const val ROUTE_FETCH_NEARBY_PLACES = "/v3/places/search"
    }

    @GET(ROUTE_FETCH_NEARBY_PLACES)
    suspend fun fetchNearbyPlaces(
        @Query(value = "ll") latLng: String,
        @Query(value = "radius") radius: Int,
    ): Response<NearbyPlacesApiResponse>

    @GET
    suspend fun fetchNextNearbyPlaces(
        @Url nextNearbyPlacesLink: String,
    ): Response<NearbyPlacesApiResponse>

}