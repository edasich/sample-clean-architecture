package com.github.edasich.place.finder.service.internal

import com.github.edasich.palce.finder.service.RemovePlaceFromFavorites
import com.github.edasich.place.finder.data.PlaceRepository
import com.github.edasich.place.finder.domain.PlaceId
import com.github.edasich.place.finder.domain.makePlaceNotFavored
import javax.inject.Inject

class RemovePlaceFromFavoritesImpl @Inject constructor(
    private val repository: PlaceRepository
) : RemovePlaceFromFavorites {

    override suspend fun invoke(placeId: PlaceId) {
        with(repository) {
            getNearbyPlaceById(placeId = placeId)?.also {
                updatePlace(place = it.makePlaceNotFavored())
            }
        }
    }

}