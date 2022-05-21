package com.github.edasich.place.finder.service.internal

import com.github.edasich.palce.finder.service.GetSearchingNearbyPlaceStatus
import com.github.edasich.place.finder.domain.SearchNearbyPlaceStatus
import com.github.edasich.place.finder.service.internal.core.CoreSearchNearbyPlaceStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchingNearbyPlaceStatusImpl @Inject constructor(
    private val coreSearchNearbyPlaceStatus: CoreSearchNearbyPlaceStatus
) : GetSearchingNearbyPlaceStatus {

    override fun invoke(): Flow<SearchNearbyPlaceStatus> {
        return coreSearchNearbyPlaceStatus.getSearchNearbyPlaceStatus()
    }

}