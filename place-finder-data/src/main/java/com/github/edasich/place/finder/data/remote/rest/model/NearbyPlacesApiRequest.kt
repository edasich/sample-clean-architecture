package com.github.edasich.place.finder.data.remote.rest.model

data class NearbyPlacesApiRequest(
    val latLng: String,
    val radius: Int
)