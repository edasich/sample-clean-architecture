package com.github.edasich.palce.finder.service

import com.github.edasich.place.finder.domain.PlaceId

interface RemovePlaceFromFavorites {
    suspend operator fun invoke(
        placeId: PlaceId
    )
}