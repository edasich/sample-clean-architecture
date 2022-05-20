package com.github.edasich.place.finder.ui.favorit.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.edasich.palce.finder.service.AddPlaceToFavorites
import com.github.edasich.palce.finder.service.GetPaginatedFavoriteNearbyPlaces
import com.github.edasich.palce.finder.service.RemovePlaceFromFavorites
import com.github.edasich.place.finder.domain.PlaceId
import com.github.edasich.place.finder.ui.nearby.mapper.NearbyPlaceMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteNearbyPlacesViewModel @Inject constructor(
    private val getPaginatedFavoriteNearbyPlaces: GetPaginatedFavoriteNearbyPlaces,
    private val addPlaceToFavorites: AddPlaceToFavorites,
    private val removePlaceFromFavorites: RemovePlaceFromFavorites,
    private val placeMapper: NearbyPlaceMapper
) : ViewModel() {

    val favoriteList = getPaginatedFavoriteNearbyPlaces
        .invoke()
        .map {
            it.map {
                placeMapper.mapToNearbyPlaceItem(place = it)
            }
        }
        .cachedIn(scope = viewModelScope)

    fun processRequest(request: FavoriteNearbyPlacesScreenRequest) {
        when (request) {
            is FavoriteNearbyPlacesScreenRequest.OnFavoritePlaceClicked -> {
                handleOnFavoritePlaceClicked(
                    request = request
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

}