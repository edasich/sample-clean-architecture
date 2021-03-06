package com.github.edasich.place.finder.data.remote.rest.model

import com.google.gson.annotations.SerializedName

data class NearbyPlacesApiResponse(
    @SerializedName(value = "context")
    val context: PlaceContextApiResponse,
    @SerializedName(value = "results")
    val places: List<PlaceApiResponse>
) {
    var nextNearbyPlacesLink: String? = null
}