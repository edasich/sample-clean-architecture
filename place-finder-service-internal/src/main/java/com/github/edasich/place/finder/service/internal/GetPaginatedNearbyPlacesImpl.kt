package com.github.edasich.place.finder.service.internal

import androidx.paging.PagingData
import com.github.edasich.location.service.GetLocationCollector
import com.github.edasich.palce.finder.service.GetAllowedPlaceDistance
import com.github.edasich.palce.finder.service.GetPaginatedNearbyPlaces
import com.github.edasich.place.finder.data.PlaceRepository
import com.github.edasich.place.finder.domain.Place
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetPaginatedNearbyPlacesImpl @Inject constructor(
    private val getLocationCollector: GetLocationCollector,
    private val getAllowedPlaceDistance: GetAllowedPlaceDistance,
    private val placeRepository: PlaceRepository
) : GetPaginatedNearbyPlaces {

    @Suppress("OPT_IN_IS_NOT_ENABLED")
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun invoke(): Flow<PagingData<Place>> {
        return getLocationCollector
            .invoke()
            .flatMapLatest {
                placeRepository.getPaginatedNearbyPlaces(
                    allowedDistance = getAllowedPlaceDistance.invoke(),
                    currentDeviceLocation = it
                )
            }
    }

}