package com.github.edasich.place.finder.ui.nearby.mapper

import com.github.edasich.place.finder.domain.Place
import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem
import com.github.edasich.place.finder.ui.nearby.model.PlaceMarkerView

interface NearbyPlaceMapper {

    fun mapToNearbyPlaceItemList(
        placeList: List<Place>
    ): List<NearbyPlaceItem>

    fun mapToPlaceMarkerViewList(
        placeList: List<Place>
    ): List<PlaceMarkerView>

}