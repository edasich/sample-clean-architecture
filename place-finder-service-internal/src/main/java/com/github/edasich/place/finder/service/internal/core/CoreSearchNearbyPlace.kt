package com.github.edasich.place.finder.service.internal.core

import com.github.edasich.location.domain.LocationConfig
import com.github.edasich.location.domain.LocationDistance
import com.github.edasich.location.service.StartLocationCollector
import com.github.edasich.location.service.StopLocationCollector
import com.github.edasich.place.finder.domain.SearchNearbyPlaceStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoreSearchNearbyPlace @Inject constructor(
    private val startLocationCollector: StartLocationCollector,
    private val stopLocationCollector: StopLocationCollector,
) : StartCoreSearchNearbyPlaceOperations,
    StopCoreSearchNearbyPlaceOperations,
    CoreSearchNearbyPlaceStatus {

    private val _searchingStatusFlow = MutableStateFlow(value = SearchNearbyPlaceStatus.STOPPED)
    private val searchingStatusFlow = _searchingStatusFlow
        .asStateFlow()

    override suspend fun start() {
        startLocationCollector.invoke(
            locationConfig = createLocationConfig()
        )
        _searchingStatusFlow.emit(value = SearchNearbyPlaceStatus.STARTED)
    }

    override suspend fun stop() {
        stopLocationCollector.invoke()
        _searchingStatusFlow.emit(value = SearchNearbyPlaceStatus.STOPPED)
    }

    override fun getSearchNearbyPlaceStatus(): Flow<SearchNearbyPlaceStatus> {
        return searchingStatusFlow
    }

    private fun createLocationConfig(): LocationConfig {
        return LocationConfig.Builder()
            .setDistance(distance = LocationDistance(distanceInMeter = 1_000))
            .build()
    }

}