package com.github.edasich.location.service.internal

import com.github.edasich.location.device.LocationDeviceManager
import com.github.edasich.location.service.StopLocationCollector
import javax.inject.Inject

class StopLocationCollectorImpl @Inject constructor(
    private val locationDeviceManager: LocationDeviceManager,
) : StopLocationCollector {

    override fun invoke() {
        locationDeviceManager.stopCollectingLocation()
    }

}