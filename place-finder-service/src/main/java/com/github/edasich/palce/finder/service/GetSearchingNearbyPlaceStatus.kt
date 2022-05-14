package com.github.edasich.palce.finder.service

import com.github.edasich.place.finder.domain.SearchNearbyPlaceStatus
import kotlinx.coroutines.flow.Flow

interface GetSearchingNearbyPlaceStatus {
    suspend operator fun invoke(): Flow<SearchNearbyPlaceStatus>
}