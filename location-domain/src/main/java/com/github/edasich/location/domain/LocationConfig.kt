package com.github.edasich.location.domain

data class LocationConfig(
    val tag: String,
    val priority: LocationPriority,
    val interval: LocationInterval,
    val fastestInterval: LocationInterval,
    val distance: LocationDistance,
)

