package com.github.edasich.place.finder.ui.nearby.model

data class NearbyPlaceItem(
    val placeId: String,
    val placeName: String,
    val placeAddress: String,
    val latitude: Double,
    val longitude: Double,
    val isFavored: Boolean
)