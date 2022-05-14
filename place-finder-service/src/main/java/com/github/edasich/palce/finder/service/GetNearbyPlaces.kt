package com.github.edasich.palce.finder.service

import com.github.edasich.base.service.PagedList
import com.github.edasich.base.service.PagedParams
import com.github.edasich.place.finder.domain.Place
import kotlinx.coroutines.flow.Flow

interface GetNearbyPlaces {
    operator fun invoke(
        pagedParams: PagedParams
    ): Flow<PagedList<Place>>
}