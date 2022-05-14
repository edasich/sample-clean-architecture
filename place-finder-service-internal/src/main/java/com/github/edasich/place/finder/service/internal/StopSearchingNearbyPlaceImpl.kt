package com.github.edasich.place.finder.service.internal

import com.github.edasich.palce.finder.service.StopSearchingNearbyPlace
import com.github.edasich.place.finder.service.internal.core.StopCoreSearchNearbyPlaceOperations
import javax.inject.Inject

class StopSearchingNearbyPlaceImpl @Inject constructor(
    private val stopCoreSearchNearbyPlaceOperations: StopCoreSearchNearbyPlaceOperations
) : StopSearchingNearbyPlace {

    override suspend fun invoke() {
        stopCoreSearchNearbyPlaceOperations.stop()
    }

}