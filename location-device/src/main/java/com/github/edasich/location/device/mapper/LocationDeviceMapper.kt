package com.github.edasich.location.device.mapper

import com.github.edasich.location.domain.DeviceLocation
import com.github.edasich.location.domain.LocationConfig
import com.github.edasich.location.domain.LocationPriority
import com.google.android.gms.location.LocationResult

interface LocationDeviceMapper {

    fun mapToDeviceLocation(
        locationResult: LocationResult,
        locationConfig: LocationConfig
    ) : DeviceLocation

    fun mapToLocationPriorityRequest(
        locationPriority: LocationPriority
    ) : Int

}