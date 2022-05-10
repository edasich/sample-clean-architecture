package com.github.edasich.location.service.internal

import com.github.edasich.location.device.LocationDeviceManager
import com.github.edasich.location.domain.LocationConfig
import com.github.edasich.location.service.StartLocationCollector
import javax.inject.Inject

class StartLocationCollectorImpl @Inject constructor(
    private val locationDeviceManager: LocationDeviceManager,
) : StartLocationCollector {

    override fun invoke(locationConfig: LocationConfig) {
        locationDeviceManager.startCollectingLocation(
            locationConfig = locationConfig
        )
    }

}