package com.github.edasich.place.finder.ui.nearby.mapper

import com.github.edasich.place.finder.domain.Place
import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem

interface NearbyPlaceMapper {

    fun mapToNearbyPlaceItemList(
        placeList: List<Place>
    ): List<NearbyPlaceItem>

    fun mapToNearbyPlaceItem(
        place: Place
    ): NearbyPlaceItem

}