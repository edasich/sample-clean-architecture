package com.github.edasich.place.finder.ui.nearby.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.edasich.base.service.PagedList
import com.github.edasich.base.service.PagedParams
import com.github.edasich.base.ui.BaseViewModel
import com.github.edasich.palce.finder.service.*
import com.github.edasich.place.finder.domain.Place
import com.github.edasich.place.finder.domain.PlaceId
import com.github.edasich.place.finder.ui.nearby.mapper.NearbyPlaceMapper
import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem
import com.github.edasich.place.finder.ui.nearby.model.PlaceMarkerView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearbyPlacesViewModel @Inject constructor(
    private val startSearchingNearbyPlace: StartSearchingNearbyPlace,
    private val stopSearchingNearbyPlace: StopSearchingNearbyPlace,
    private val getSearchingNearbyPlaceStatus: GetSearchingNearbyPlaceStatus,
    private val getNearbyPlaces: GetNearbyPlaces,
    private val addPlaceToFavorites: AddPlaceToFavorites,
    private val removePlaceFromFavorites: RemovePlaceFromFavorites,
    private val nearbyPlaceMapper: NearbyPlaceMapper
) : BaseViewModel<
        NearbyPlacesScreenRequest,
        NearbyPlacesScreenState,
        NearbyPlacesScreenEvent,
        NearbyPlacesScreenUi,
        >() {

    private val _pagedParamsFlow = MutableStateFlow(value = PagedParams(limit = 10, 0))

    override val _state: MutableLiveData<NearbyPlacesScreenUi> =
        MutableLiveData(NearbyPlacesScreenUi())

    init {
        startSearching()
        updateNearbyPlacesResult()
    }

    override fun processRequest(request: NearbyPlacesScreenRequest) {
        when (request) {
            is NearbyPlacesScreenRequest.OnLoadMore -> {
            }
            is NearbyPlacesScreenRequest.OnPlaceItemViewed -> {
                handleOnPlaceItemViewed(
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
            is NearbyPlacesScreenState.InvalidateLoading -> {
                _state.value = _state.value!!.copy(isLoading = state.isLoading)
            }
            is NearbyPlacesScreenState.InvalidateFreshNearbyPlaceList -> {
                _state.value = _state.value!!.copy(
                    placeList = state.placeList,
                    placeMarkerViewList = state.placeMarkerViewList
                )
            }
            is NearbyPlacesScreenState.InvalidatePartialNearbyPlaceList -> {
                val updatedNearbyPlaceItemList = _state.value!!.placeList.toMutableList()
                updatedNearbyPlaceItemList.addAll(elements = state.placeList)
                val updatedPlaceMarkerViewList = _state.value!!.placeMarkerViewList.toMutableList()
                updatedPlaceMarkerViewList.addAll(elements = state.placeMarkerViewList)
                _state.value = _state.value!!.copy(
                    placeList = updatedNearbyPlaceItemList,
                    placeMarkerViewList = updatedPlaceMarkerViewList
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
        viewModelScope.launch {
            _pagedParamsFlow
                .asStateFlow()
                .flatMapLatest {
                    getNearbyPlaces(
                        pagedParams = it
                    )
                }
                .collect {
                    Log.i("===>", "getNearbyPlacesResult: $it")
                    when (it) {
                        is PagedList.FreshLoaded -> {
                            invalidateLoadingState(isLoading = false)

                            val nearbyPlaceItems = mapToNearbyPlaceItemList(
                                nearbyPlaceItemList = it.items
                            )
                            val placeMarkerViewList = mapToPlaceMarkerViewList(
                                nearbyPlaceItemList = it.items
                            )
                            invalidateFreshNearbyPlaceListState(
                                nearbyPlaceItemList = nearbyPlaceItems,
                                placeMarkerViewList = placeMarkerViewList
                            )

                            placeMarkerViewList.firstOrNull()?.also { firstPlaceMarker ->
                                showPlaceMarkerView(markerView = firstPlaceMarker)
                            }
                        }
                        is PagedList.PartialLoaded -> {
                            invalidateLoadingState(isLoading = false)

                            val nearbyPlaceItems = mapToNearbyPlaceItemList(
                                nearbyPlaceItemList = it.items
                            )
                            val placeMarkerViewList = mapToPlaceMarkerViewList(
                                nearbyPlaceItemList = it.items
                            )
                            invalidatePartialNearbyPlaceListState(
                                nearbyPlaceItemList = nearbyPlaceItems,
                                placeMarkerViewList = placeMarkerViewList
                            )

                            placeMarkerViewList.firstOrNull()?.also { firstPlaceMarker ->
                                showPlaceMarkerView(markerView = firstPlaceMarker)
                            }
                        }
                        PagedList.Loading -> {
                            invalidateLoadingState(isLoading = true)
                        }
                    }
                }
        }
    }

    private fun handleOnPlaceItemViewed(
        request: NearbyPlacesScreenRequest.OnPlaceItemViewed
    ) {
        _state.value!!.placeList[request.placePosition].also {
            showPlaceMarkerView(placeItem = it)
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
            event = NearbyPlacesScreenEvent.ShowPlaceMarkerView(
                markerLatitude = placeItem.latitude,
                markerLongitude = placeItem.longitude
            )
        )
    }

    private fun showPlaceMarkerView(
        markerView: PlaceMarkerView
    ) {
        sendEvent(
            event = NearbyPlacesScreenEvent.ShowPlaceMarkerView(
                markerLatitude = markerView.latitude,
                markerLongitude = markerView.longitude
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
            .placeList
            .find {
                it.latitude == latitude && it.longitude == longitude
            }
            ?.let {
                _state.value!!.placeList.indexOf(element = it)
            }
            ?: run {
                -1
            }
    }

    private fun mapToNearbyPlaceItemList(
        nearbyPlaceItemList: List<Place>
    ): List<NearbyPlaceItem> {
        return nearbyPlaceMapper.mapToNearbyPlaceItemList(
            placeList = nearbyPlaceItemList
        )
    }

    private fun mapToPlaceMarkerViewList(
        nearbyPlaceItemList: List<Place>
    ): List<PlaceMarkerView> {
        return nearbyPlaceMapper.mapToPlaceMarkerViewList(
            placeList = nearbyPlaceItemList
        )
    }

    private fun invalidateLoadingState(
        isLoading: Boolean
    ) {
        setState(
            state = NearbyPlacesScreenState.InvalidateLoading(isLoading = isLoading)
        )
    }

    private fun invalidateFreshNearbyPlaceListState(
        nearbyPlaceItemList: List<NearbyPlaceItem>,
        placeMarkerViewList: List<PlaceMarkerView>
    ) {
        setState(
            state = NearbyPlacesScreenState.InvalidateFreshNearbyPlaceList(
                placeList = nearbyPlaceItemList,
                placeMarkerViewList = placeMarkerViewList
            )
        )
    }

    private fun invalidatePartialNearbyPlaceListState(
        nearbyPlaceItemList: List<NearbyPlaceItem>,
        placeMarkerViewList: List<PlaceMarkerView>
    ) {
        setState(
            state = NearbyPlacesScreenState.InvalidatePartialNearbyPlaceList(
                placeList = nearbyPlaceItemList,
                placeMarkerViewList = placeMarkerViewList
            )
        )
    }

}