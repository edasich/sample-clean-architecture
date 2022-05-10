package com.github.edasich.location.device.mapper.internal

import com.github.edasich.location.device.mapper.LocationDeviceMapper
import com.github.edasich.location.domain.*
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import javax.inject.Inject

class LocationDeviceMapperImpl @Inject constructor() : LocationDeviceMapper {

    override fun mapToDeviceLocation(
        locationResult: LocationResult,
        locationConfig: LocationConfig
    ): DeviceLocation {
        return DeviceLocation(
            latitude = Latitude(latitude = locationResult.lastLocation.latitude),
            longitude = Longitude(longitude = locationResult.lastLocation.longitude),
            time = LocationTime(timeInMillis = locationResult.lastLocation.time),
            accuracy = LocationAccuracy(accuracyInMeter = locationResult.lastLocation.accuracy),
            currentConfig = locationConfig
        )
    }

    override fun mapToLocationPriorityRequest(
        locationPriority: LocationPriority
    ): Int {
        return when (locationPriority) {
            LocationPriority.GPS_WIFI_MOBILE_CELL_PASSIVE -> LocationRequest.PRIORITY_HIGH_ACCURACY
            LocationPriority.WIFI_MOBILE_CELL_PASSIVE -> LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            LocationPriority.CELL_PASSIVE -> LocationRequest.PRIORITY_LOW_POWER
            LocationPriority.PASSIVE -> LocationRequest.PRIORITY_NO_POWER
        }
    }

}