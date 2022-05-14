package com.github.edasich.place.finder.data.remote.rest.model

import com.google.gson.annotations.SerializedName

data class PlaceContextApiResponse(
    @SerializedName(value = "geo_bounds")
    val geoBound: GeoBoundApiResponse
)

data class GeoBoundApiResponse(
    @SerializedName(value = "circle")
    val circleBound: CircleGeoBoundApiResponse
)

data class CircleGeoBoundApiResponse(
    @SerializedName(value = "center")
    val center: CenterCircleGeoBoundApiResponse,
    @SerializedName(value = "radius")
    val radius: Int
)

data class CenterCircleGeoBoundApiResponse(
    @SerializedName(value = "latitude")
    val latitude: Double,
    @SerializedName(value = "longitude")
    val longitude: Double
)