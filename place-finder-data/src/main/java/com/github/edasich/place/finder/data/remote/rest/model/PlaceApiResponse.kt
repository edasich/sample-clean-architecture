package com.github.edasich.place.finder.data.remote.rest.model

import com.google.gson.annotations.SerializedName

data class PlaceApiResponse(
    @SerializedName(value = "fsq_id")
    val id: String,
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "geocodes")
    val geocode: GeocodeApiResponse,
    @SerializedName(value = "location")
    val location: LocationApiResponse,
)

data class GeocodeApiResponse(
    @SerializedName(value = "main")
    val main: MainGeocodeApiResponse
)

data class MainGeocodeApiResponse(
    @SerializedName(value = "latitude")
    val latitude: Double,
    @SerializedName(value = "longitude")
    val longitude: Double
)

data class LocationApiResponse(
    @SerializedName(value = "formatted_address")
    val formattedAddress: String
)