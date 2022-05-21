package com.github.edasich.place.finder.ui.nearby.view

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.edasich.palce.finder.service.*
import com.github.edasich.place.finder.domain.PlaceId
import com.github.edasich.place.finder.domain.SearchNearbyPlaceStatus
import com.github.edasich.place.finder.ui.nearby.mapper.NearbyPlaceMapper
import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem
import com.github.edasich.place.finder.ui.nearby.model.findNearbyPlaceByLatLng
import com.github.edasich.place.finder.ui.nearby.model.findNearbyPlaceByPosition
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearbyPlacesViewModel @Inject constructor(
    private val application: Application,
    private val startSearchingNearbyPlace: StartSearchingNearbyPlace,
    private val stopSearchingNearbyPlace: StopSearchingNearbyPlace,
    private val getSearchingNearbyPlaceStatus: GetSearchingNearbyPlaceStatus,
    private val getNearbyPlaces: GetNearbyPlaces,
    private val getPaginatedNearbyPlaces: GetPaginatedNearbyPlaces,
    private val addPlaceToFavorites: AddPlaceToFavorites,
    private val removePlaceFromFavorites: RemovePlaceFromFavorites,
    private val nearbyPlaceMapper: NearbyPlaceMapper
) : ViewModel() {

    val searchingPlaceFlow = getSearchingNearbyPlaceStatus
        .invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SearchNearbyPlaceStatus.STOPPED
        )

    val markerListFlow: StateFlow<List<NearbyPlaceItem>> = getNearbyPlaces
        .invoke()
        .map {
            nearbyPlaceMapper.mapToNearbyPlaceItemList(placeList = it)
        }
        .onEach {
            it.firstOrNull()?.also {
                showPlaceMarkerView(placeItem = it)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val placeListFlow: Flow<PagingData<NearbyPlaceItem>> = getPaginatedNearbyPlaces
        .invoke()
        .map {
            it.map {
                nearbyPlaceMapper.mapToNearbyPlaceItem(place = it)
            }
        }
        .cachedIn(scope = viewModelScope)


    private val _events: MutableSharedFlow<NearbyPlacesScreenEvent> = MutableSharedFlow()
    val events: SharedFlow<NearbyPlacesScreenEvent> = _events.asSharedFlow()

    fun processRequest(request: NearbyPlacesScreenRequest) {
        when (request) {
            NearbyPlacesScreenRequest.OnSearchPlaceClicked -> {
                handleOnSearchPlaceClicked()
            }
            NearbyPlacesScreenRequest.OnLocationPermissionsGranted -> {
                handleOnLocationPermissionsGranted()
            }
            NearbyPlacesScreenRequest.OnLocationPermissionsDenied -> {
                handleOnLocationPermissionsDenied()
            }
            NearbyPlacesScreenRequest.OnLocationAcceptedToBeEnabled -> {
                handleOnLocationAcceptedToBeEnabled()
            }
            NearbyPlacesScreenRequest.OnLocationDeniedToBeEnabled -> {

            }
            is NearbyPlacesScreenRequest.OnPlaceItemScrolled -> {
                handleOnPlaceItemScrolled(
                    request = request
                )
            }
            is NearbyPlacesScreenRequest.OnPlaceMarkerClicked -> {
                handleOnPlaceMarkerClicked(
                    request = request
                )
            }
            is NearbyPlacesScreenRequest.OnFavoritePlaceClicked -> {
                handleOnFavoritePlaceClicked(
                    request = request
                )
            }
        }
    }

    private fun handleOnSearchPlaceClicked() {
        when (searchingPlaceFlow.value) {
            SearchNearbyPlaceStatus.STARTED -> {
                stopSearching()
            }
            SearchNearbyPlaceStatus.STOPPED -> {
                tryStartSearching()
            }
        }
    }

    private fun handleOnLocationPermissionsGranted() {
        tryStartSearching()
    }

    private fun handleOnLocationPermissionsDenied() {
        stopSearching()
    }

    private fun handleOnLocationAcceptedToBeEnabled() {
        tryStartSearching()
    }

    private fun handleOnLocationDeniedToBeEnabled() {
        stopSearching()
    }

    private fun handleOnPlaceItemScrolled(
        request: NearbyPlacesScreenRequest.OnPlaceItemScrolled
    ) {
        markerListFlow.value.findNearbyPlaceByPosition(
            position = request.placePosition
        ) { nearbyPlace, _ ->
            showPlaceMarkerView(placeItem = nearbyPlace)
        }
    }

    private fun handleOnPlaceMarkerClicked(
        request: NearbyPlacesScreenRequest.OnPlaceMarkerClicked
    ) {
        markerListFlow.value.findNearbyPlaceByLatLng(
            latitude = request.markerLatitude,
            longitude = request.markerLongitude
        ) { _, position ->
            showPlaceItem(placePosition = position)
        }
    }

    private fun handleOnFavoritePlaceClicked(
        request: NearbyPlacesScreenRequest.OnFavoritePlaceClicked
    ) {
        viewModelScope.launch {
            with(request.place) {
                if (isFavored) {
                    removePlaceFromFavorites.invoke(placeId = PlaceId(id = placeId))
                } else {
                    addPlaceToFavorites.invoke(placeId = PlaceId(id = placeId))
                }
            }
        }
    }

    private fun showPlaceMarkerView(
        placeItem: NearbyPlaceItem
    ) {
        sendEvent(
            event = NearbyPlacesScreenEvent.ShowPlaceMarker(
                markerLatitude = placeItem.latitude,
                markerLongitude = placeItem.longitude
            )
        )
    }

    private fun showPlaceItem(placePosition: Int) {
        sendEvent(
            event = NearbyPlacesScreenEvent.ShowPlaceItem(
                placePosition = placePosition
            )
        )
    }

    private fun sendEvent(event: NearbyPlacesScreenEvent) {
        viewModelScope.launch {
            _events.emit(value = event)
        }
    }

    private fun tryStartSearching() {
        if (!isLocationPermissionGranted()) {
            sendEvent(
                event = NearbyPlacesScreenEvent.AskGrantLocationPermissions
            )
            return
        }

        if (!isLocationEnabled()) {
            val lReq: LocationRequest = LocationRequest.create()
            lReq.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val addLocationRequest = LocationSettingsRequest
                .Builder()
                .addLocationRequest(lReq)
            val settingsClient = LocationServices.getSettingsClient(application)
            val checkLocationSettings =
                settingsClient.checkLocationSettings(addLocationRequest.build())
            checkLocationSettings.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            if (exception is ResolvableApiException) {
                                try {
                                    sendEvent(
                                        event = NearbyPlacesScreenEvent.AskEnableLocation(
                                            pendingIntent = exception.resolution
                                        )
                                    )
                                } catch (exception: IntentSender.SendIntentException) {
                                    // Ignore the error.
                                    exception.printStackTrace()
                                } catch (exception: Exception) {
                                    // Ignore the error.
                                    exception.printStackTrace()
                                }
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                        }
                    }
                }
            }
            return
        }

        startSearching()
    }

    private fun startSearching() {
        viewModelScope.launch {
            startSearchingNearbyPlace()
        }
    }

    private fun stopSearching() {
        viewModelScope.launch {
            stopSearchingNearbyPlace()
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        val accessFineLocationPermissionStatus =
            ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        val accessCoarseLocationPermissionStatus =
            ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        return accessFineLocationPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                accessCoarseLocationPermissionStatus == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

}