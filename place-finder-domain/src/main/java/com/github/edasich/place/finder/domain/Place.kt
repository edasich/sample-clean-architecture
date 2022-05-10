package com.github.edasich.place.finder.domain

data class Place(
    val id: PlaceId,
    val name: PlaceName,
    val address: PlaceAddress,
    val placeFavoriteStatus: PlaceFavoriteStatus
)