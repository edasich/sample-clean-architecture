package com.github.edasich.place.finder.service.internal.core

import com.github.edasich.place.finder.domain.SearchNearbyPlaceStatus
import kotlinx.coroutines.flow.Flow

interface CoreSearchNearbyPlaceStatus {
    fun getSearchNearbyPlaceStatus(): Flow<SearchNearbyPlaceStatus>
}