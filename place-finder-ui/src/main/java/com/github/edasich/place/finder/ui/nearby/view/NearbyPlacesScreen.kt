package com.github.edasich.place.finder.ui.nearby.view

import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem
import com.github.edasich.place.finder.ui.nearby.model.PlaceMarkerView

sealed class NearbyPlacesScreenRequest {
    data class OnLoadMore(
        val totalLoadedItemCount: Int
    ) : NearbyPlacesScreenRequest()

    data class OnPlaceItemViewed(
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
    data class InvalidateLoading(
        val isLoading: Boolean
    ) : NearbyPlacesScreenState()

    data class InvalidateFreshNearbyPlaceList(
        val placeList: List<NearbyPlaceItem>,
        val placeMarkerViewList: List<PlaceMarkerView>
    ) : NearbyPlacesScreenState()

    data class InvalidatePartialNearbyPlaceList(
        val placeList: List<NearbyPlaceItem>,
        val placeMarkerViewList: List<PlaceMarkerView>
    ) : NearbyPlacesScreenState()
}

sealed class NearbyPlacesScreenEvent {
    data class ShowPlaceItem(
        val placePosition: Int,
    ) : NearbyPlacesScreenEvent()

    data class ShowPlaceMarkerView(
        val markerLatitude: Double,
        val markerLongitude: Double
    ) : NearbyPlacesScreenEvent()
}

data class NearbyPlacesScreenUi(
    val isLoading: Boolean = false,
    val placeList: List<NearbyPlaceItem> = emptyList(),
    val placeMarkerViewList: List<PlaceMarkerView> = emptyList()
)