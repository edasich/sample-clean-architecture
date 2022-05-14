package com.github.edasich.place.finder.service.internal

import com.github.edasich.palce.finder.service.AddPlaceToFavorites
import com.github.edasich.place.finder.data.PlaceRepository
import com.github.edasich.place.finder.domain.Place
import com.github.edasich.place.finder.domain.PlaceId
import com.github.edasich.place.finder.domain.makePlaceFavored
import com.github.edasich.place.finder.domain.makePlaceNotFavored
import javax.inject.Inject

class AddPlaceToFavoritesImpl @Inject constructor(
    private val repository: PlaceRepository
) : AddPlaceToFavorites {

    override suspend fun invoke(placeId: PlaceId) {
        with(repository) {
            getNearbyPlaceById(placeId = placeId)?.also {
                updatePlace(place = it.makePlaceFavored())
            }
        }
    }

}