package com.github.edasich.location.domain

data class DeviceLocation(
    val latitude: Latitude,
    val longitude: Longitude,
    val time: LocationTime,
    val accuracy: LocationAccuracy,
    val currentConfig: LocationConfig
)
