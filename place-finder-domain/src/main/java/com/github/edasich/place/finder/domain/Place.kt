package com.github.edasich.place.finder.domain

data class Place(
    val id: PlaceId,
    val name: PlaceName,
    val address: PlaceAddress,
    val placeFavoriteStatus: PlaceFavoriteStatus
)

fun Place.makePlaceFavored(): Place {
    return this.copy(placeFavoriteStatus = PlaceFavoriteStatus.Favored)
}

fun Place.makePlaceNotFavored(): Place {
    return this.copy(placeFavoriteStatus = PlaceFavoriteStatus.NotFavored)
}