package com.github.edasich.place.finder.ui.nearby.view

import android.app.PendingIntent
import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem

sealed class NearbyPlacesScreenRequest {
    object OnSearchPlaceClicked : NearbyPlacesScreenRequest()
    object OnLocationPermissionsGranted : NearbyPlacesScreenRequest()
    object OnLocationPermissionsDenied : NearbyPlacesScreenRequest()
    object OnLocationAcceptedToBeEnabled : NearbyPlacesScreenRequest()
    object OnLocationDeniedToBeEnabled : NearbyPlacesScreenRequest()

    data class OnPlaceItemScrolled(
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

sealed class NearbyPlacesScreenEvent {
    object AskGrantLocationPermissions : NearbyPlacesScreenEvent()

    data class AskEnableLocation(
        val pendingIntent: PendingIntent
    ) : NearbyPlacesScreenEvent()

    data class ShowPlaceItem(
        val placePosition: Int,
    ) : NearbyPlacesScreenEvent()

    data class ShowPlaceMarker(
        val markerLatitude: Double,
        val markerLongitude: Double
    ) : NearbyPlacesScreenEvent()
}