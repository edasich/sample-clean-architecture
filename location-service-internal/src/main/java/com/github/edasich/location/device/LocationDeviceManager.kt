package com.github.edasich.location.device

import kotlinx.coroutines.flow.Flow
import com.github.edasich.location.domain.DeviceLocation
import com.github.edasich.location.domain.LocationConfig

interface LocationDeviceManager {

    fun startCollectingLocation(locationConfig: LocationConfig)

    fun stopCollectingLocation()

    fun getLocationCollector(): Flow<DeviceLocation>

    fun isAccessLocationPermissionGranted(): Boolean

}