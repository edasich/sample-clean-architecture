package com.github.edasich.location.service.internal

import com.github.edasich.location.device.LocationDeviceManager
import com.github.edasich.location.domain.DeviceLocation
import com.github.edasich.location.service.GetLocationCollector
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationCollectorImpl @Inject constructor(
    private val locationDeviceManager: LocationDeviceManager
) : GetLocationCollector {

    override fun invoke(): Flow<DeviceLocation> {
        return locationDeviceManager.getLocationCollector()
    }

}