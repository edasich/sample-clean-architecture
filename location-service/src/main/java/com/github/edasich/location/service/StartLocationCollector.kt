package com.github.edasich.location.service

import com.github.edasich.location.domain.LocationConfig

interface StartLocationCollector {
    operator fun invoke(
        locationConfig: LocationConfig = LocationConfig.Builder().build()
    )
}