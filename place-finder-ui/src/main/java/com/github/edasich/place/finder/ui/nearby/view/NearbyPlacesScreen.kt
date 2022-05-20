package com.github.edasich.place.finder.ui.nearby.view

import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem

sealed class NearbyPlacesScreenRequest {
    data class OnPlaceItemScrolled(
        val placeList : List<NearbyPlaceItem>,
        val placePosition: Int
    ) : NearbyPlacesScreenRequest()

    data class OnPlaceMarkerClicked(
        val markerLatitude: Double,
        val markerLongitude: Double
    ) : NearbyPlacesScreenRequest()

    data class OnFavoritePlaceClicked(
        val place: NearbyPlaceItem
    ) : NearbyPlacesScreenRequest()
}

sealed class NearbyPlacesScreenState {
    data class InvalidateNearbyPlaceItemList(
        val placeMarkerViewList: List<NearbyPlaceItem>
    ) : NearbyPlacesScreenState()
}

sealed class NearbyPlacesScreenEvent {
    data class ShowPlaceItem(
        val placePosition: Int,
    ) : NearbyPlacesScreenEvent()

    data class ShowPlaceMarker(
        val markerLatitude: Double,
        val markerLongitude: Double
    ) : NearbyPlacesScreenEvent()
}

data class NearbyPlacesScreenUi(
    val placeMarkerList: List<NearbyPlaceItem> = emptyList()
)