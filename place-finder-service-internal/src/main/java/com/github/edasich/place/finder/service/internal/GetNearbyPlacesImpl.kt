package com.github.edasich.place.finder.service.internal

import com.github.edasich.base.service.PagedList
import com.github.edasich.base.service.PagedParams
import com.github.edasich.location.service.GetLocationCollector
import com.github.edasich.palce.finder.service.GetAllowedPlaceDistance
import com.github.edasich.palce.finder.service.GetNearbyPlaces
import com.github.edasich.place.finder.data.PlaceRepository
import com.github.edasich.place.finder.domain.Place
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GetNearbyPlacesImpl @Inject constructor(
    private val getLocationCollector: GetLocationCollector,
    private val getAllowedPlaceDistance: GetAllowedPlaceDistance,
    private val placeRepository: PlaceRepository
) : GetNearbyPlaces {

    @Suppress("OPT_IN_IS_NOT_ENABLED")
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun invoke(
        pagedParams: PagedParams
    ): Flow<PagedList<Place>> {
        return getLocationCollector
            .invoke()
            .flatMapLatest {
                placeRepository.getNearbyPlaces(
                    allowedDistance = getAllowedPlaceDistance.invoke(),
                    currentDeviceLocation = it
                )
            }
    }

}