package com.github.edasich.palce.finder.service

import androidx.paging.PagingData
import com.github.edasich.base.service.PagedList
import com.github.edasich.base.service.PagedParams
import com.github.edasich.place.finder.domain.Place
import kotlinx.coroutines.flow.Flow

interface GetPaginatedNearbyPlaces {
    operator fun invoke(): Flow<PagingData<Place>>
}