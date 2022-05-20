package com.github.edasich.place.finder.ui.nearby.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.edasich.base.ui.BaseViewModel
import com.github.edasich.palce.finder.service.*
import com.github.edasich.place.finder.domain.PlaceId
import com.github.edasich.place.finder.ui.nearby.mapper.NearbyPlaceMapper
import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
) : BaseViewModel<
        NearbyPlacesScreenRequest,
        NearbyPlacesScreenState,
        NearbyPlacesScreenEvent,
        NearbyPlacesScreenUi,
        >() {

    override val _state: MutableLiveData<NearbyPlacesScreenUi> =
        MutableLiveData(NearbyPlacesScreenUi())

    val placeList: Flow<PagingData<NearbyPlaceItem>> = getPaginatedNearbyPlaces
        .invoke()
        .map {
            it.map {
                nearbyPlaceMapper.mapToNearbyPlaceItem(place = it)
            }
        }
        .cachedIn(scope = viewModelScope)

    init {
        startSearching()
    }

    override fun processRequest(request: NearbyPlacesScreenRequest) {
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

    override fun setState(state: NearbyPlacesScreenState) {
        when (state) {
            is NearbyPlacesScreenState.InvalidateNearbyPlaceItemList -> {
                _state.value = _state.value!!.copy(
                    placeMarkerList = state.placeMarkerViewList
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

    private fun updateNearbyPlacesResult() {
    }

    private fun handleOnPlaceItemScrolled(
        request: NearbyPlacesScreenRequest.OnPlaceItemScrolled
    ) {
        setState(
            state = NearbyPlacesScreenState.InvalidateNearbyPlaceItemList(
                placeMarkerViewList = request.placeList
            )
        )
        if (request.placeList.isNotEmpty()) {
            showPlaceMarkerView(placeItem = request.placeList.get(request.placePosition))
        }
    }

    private fun handleOnPlaceMarkerClicked(
        request: NearbyPlacesScreenRequest.OnPlaceMarkerClicked
    ) {
        findPlaceItemPositionByLatLng(
            latitude = request.markerLatitude,
            longitude = request.markerLongitude
        )
            .takeIf {
                it >= 0
            }
            ?.also {
                showPlaceItem(placePosition = it)
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

    private fun findPlaceItemPositionByLatLng(
        latitude: Double,
        longitude: Double
    ): Int {
        return _state.value!!
            .placeMarkerList
            .find {
                it.latitude == latitude && it.longitude == longitude
            }
            ?.let {
                _state.value!!.placeMarkerList.indexOf(element = it)
            }
            ?: run {
                -1
            }
    }

}