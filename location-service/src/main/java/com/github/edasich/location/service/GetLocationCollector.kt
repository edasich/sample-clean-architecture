package com.github.edasich.location.service

import com.github.edasich.location.domain.DeviceLocation
import kotlinx.coroutines.flow.Flow

interface GetLocationCollector {
    operator fun invoke(): Flow<DeviceLocation>
}