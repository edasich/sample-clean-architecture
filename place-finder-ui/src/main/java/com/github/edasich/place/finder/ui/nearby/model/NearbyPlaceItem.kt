package com.github.edasich.place.finder.ui.nearby.model

data class NearbyPlaceItem(
    val placeId: String,
    val placeName: String,
    val placeAddress: String,
    val latitude: Double,
    val longitude: Double,
    val isFavored: Boolean
)

fun List<NearbyPlaceItem>.findNearbyPlaceByLatLng(
    latitude: Double,
    longitude: Double,
    onFound: (nearbyPlace: NearbyPlaceItem, position: Int) -> Unit
) {
    find {
        it.latitude == latitude && it.longitude == longitude
    }?.also {
        onFound(it, indexOf(element = it))
    }
}

fun List<NearbyPlaceItem>.findNearbyPlaceByPosition(
    position: Int,
    onFound: (nearbyPlace: NearbyPlaceItem, position: Int) -> Unit
) {
    getOrNull(index = position)?.also {
        onFound(it, position)
    }
}