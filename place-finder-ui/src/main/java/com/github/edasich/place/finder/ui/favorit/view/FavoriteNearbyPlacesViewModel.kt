package com.github.edasich.place.finder.ui.favorit.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.edasich.base.ui.BaseViewModel
import com.github.edasich.palce.finder.service.AddPlaceToFavorites
import com.github.edasich.palce.finder.service.GetFavoriteNearbyPlaces
import com.github.edasich.palce.finder.service.RemovePlaceFromFavorites
import com.github.edasich.place.finder.domain.PlaceId
import com.github.edasich.place.finder.ui.nearby.mapper.NearbyPlaceMapper
import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteNearbyPlacesViewModel @Inject constructor(
    private val getFavoriteNearbyPlaces: GetFavoriteNearbyPlaces,
    private val addPlaceToFavorites: AddPlaceToFavorites,
    private val removePlaceFromFavorites: RemovePlaceFromFavorites,
    private val placeMapper: NearbyPlaceMapper
) : BaseViewModel<
        FavoriteNearbyPlacesScreenRequest,
        FavoriteNearbyPlacesScreenState,
        FavoriteNearbyPlacesScreenEvent,
        FavoriteNearbyPlacesScreenUi,
        >() {

    override val _state: MutableLiveData<FavoriteNearbyPlacesScreenUi> =
        MutableLiveData(FavoriteNearbyPlacesScreenUi())

    init {
        updateFavoriteNearbyPlacesResult()
    }

    override fun processRequest(request: FavoriteNearbyPlacesScreenRequest) {
        when (request) {
            is FavoriteNearbyPlacesScreenRequest.OnFavoritePlaceClicked -> {
                handleOnFavoritePlaceClicked(
                    request = request
                )
            }
        }
    }

    override fun setState(state: FavoriteNearbyPlacesScreenState) {
        when (state) {
            is FavoriteNearbyPlacesScreenState.InvalidateFavoriteNearbyPlaceList -> {
                _state.value = _state.value!!.copy(
                    isFavoritePlaceListEmptyHolderVisible = state.favoritePlaceList.isEmpty(),
                    favoritePlaceList = state.favoritePlaceList
                )
            }
        }
    }

    private fun handleOnFavoritePlaceClicked(
        request: FavoriteNearbyPlacesScreenRequest.OnFavoritePlaceClicked
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

    private fun updateFavoriteNearbyPlacesResult() {
        viewModelScope.launch {
            getFavoriteNearbyPlaces()
                .map {
                    placeMapper.mapToNearbyPlaceItemList(placeList = it)
                }
                .collect {
                    invalidateFavoriteNearbyPlaceList(
                        favoriteNearbyPlaceItemList = it
                    )
                }
        }
    }

    private fun invalidateFavoriteNearbyPlaceList(
        favoriteNearbyPlaceItemList: List<NearbyPlaceItem>
    ) {
        setState(
            state = FavoriteNearbyPlacesScreenState.InvalidateFavoriteNearbyPlaceList(
                favoritePlaceList = favoriteNearbyPlaceItemList
            )
        )
    }

}