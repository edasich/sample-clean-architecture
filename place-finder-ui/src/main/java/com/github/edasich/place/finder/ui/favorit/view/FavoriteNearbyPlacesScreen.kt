package com.github.edasich.place.finder.ui.favorit.view

import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem

sealed class FavoriteNearbyPlacesScreenRequest {
    data class OnFavoritePlaceClicked(
        val place: NearbyPlaceItem
    ) : FavoriteNearbyPlacesScreenRequest()
}