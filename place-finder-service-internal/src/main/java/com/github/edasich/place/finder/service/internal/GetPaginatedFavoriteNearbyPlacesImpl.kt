package com.github.edasich.place.finder.service.internal

import androidx.paging.PagingData
import com.github.edasich.palce.finder.service.GetPaginatedFavoriteNearbyPlaces
import com.github.edasich.place.finder.data.PlaceRepository
import com.github.edasich.place.finder.domain.Place
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaginatedFavoriteNearbyPlacesImpl @Inject constructor(
    private val repository: PlaceRepository
) : GetPaginatedFavoriteNearbyPlaces {

    override fun invoke(): Flow<PagingData<Place>> {
        return repository.getPaginatedFavoriteNearbyPlaces()
    }

}