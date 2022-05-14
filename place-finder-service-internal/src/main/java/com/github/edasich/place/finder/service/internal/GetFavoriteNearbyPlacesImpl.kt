package com.github.edasich.place.finder.service.internal

import com.github.edasich.palce.finder.service.GetFavoriteNearbyPlaces
import com.github.edasich.place.finder.data.PlaceRepository
import com.github.edasich.place.finder.domain.Place
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteNearbyPlacesImpl @Inject constructor(
    private val repository: PlaceRepository
) : GetFavoriteNearbyPlaces {

    override fun invoke(): Flow<List<Place>> {
        return repository.getFavoriteNearbyPlaces()
    }

}