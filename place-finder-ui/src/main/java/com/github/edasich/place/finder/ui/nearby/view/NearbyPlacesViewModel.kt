package com.github.edasich.place.finder.ui.nearby.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.edasich.palce.finder.service.*
import com.github.edasich.place.finder.domain.PlaceId
import com.github.edasich.place.finder.ui.nearby.mapper.NearbyPlaceMapper
import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem
import com.github.edasich.place.finder.ui.nearby.model.findNearbyPlaceByLatLng
import com.github.edasich.place.finder.ui.nearby.model.findNearbyPlaceByPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearbyPlacesViewModel @Inject constructor(
    private val startSearchingNearbyPlace: StartSearchingNearbyPlace,
    private val stopSearchingNearbyPlace: StopSearchingNearbyPlace,
    private val getSearchingNearbyPlaceStatus: GetSearchingNearbyPlaceStatus,
    private val getNearbyPlaces: GetNearbyPlaces,
    private val getPaginatedNearbyPlaces: GetPaginatedNearbyPlaces,
    private val addPlaceToFavorites: AddPlaceToFavorites,
    private val removePlaceFromFavorites: RemovePlaceFromFavorites,
    private val nearbyPlaceMapper: NearbyPlaceMapper
) : ViewModel() {

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

    init {
        startSearching()
    }

    fun processRequest(request: NearbyPlacesScreenRequest) {
        when (request) {
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

}