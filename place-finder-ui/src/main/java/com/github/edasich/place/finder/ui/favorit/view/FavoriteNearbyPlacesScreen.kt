package com.github.edasich.place.finder.ui.favorit.view

import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem

sealed class FavoriteNearbyPlacesScreenRequest {
    data class OnFavoritePlaceClicked(
        val place: NearbyPlaceItem
    ) : FavoriteNearbyPlacesScreenRequest()
}

sealed class FavoriteNearbyPlacesScreenState {
    data class InvalidateFavoriteNearbyPlaceList(
        val favoritePlaceList: List<NearbyPlaceItem>,
    ) : FavoriteNearbyPlacesScreenState()
}

sealed class FavoriteNearbyPlacesScreenEvent {
}

data class FavoriteNearbyPlacesScreenUi(
    val isFavoritePlaceListEmptyHolderVisible: Boolean = true,
    val favoritePlaceList: List<NearbyPlaceItem> = emptyList(),
)