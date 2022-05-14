package com.github.edasich.place.finder.ui.nearby.model

data class PlaceMarkerView(
    val placeId: String,
    val latitude: Double,
    val longitude: Double,
    val isFavored: Boolean
)