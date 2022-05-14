package com.github.edasich.place.finder.service.internal

import com.github.edasich.palce.finder.service.StartSearchingNearbyPlace
import com.github.edasich.place.finder.service.internal.core.StartCoreSearchNearbyPlaceOperations
import javax.inject.Inject

class StartSearchingNearbyPlaceImpl @Inject constructor(
    private val startCoreSearchNearbyPlaceOperations: StartCoreSearchNearbyPlaceOperations
) : StartSearchingNearbyPlace {

    override suspend fun invoke() {
        startCoreSearchNearbyPlaceOperations.start()
    }

}