package com.github.edasich.place.finder.ui.nearby.mapper.internal

import com.github.edasich.place.finder.domain.Place
import com.github.edasich.place.finder.domain.PlaceFavoriteStatus
import com.github.edasich.place.finder.ui.nearby.mapper.NearbyPlaceMapper
import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem
import javax.inject.Inject

class NearbyPlaceMapperImpl @Inject constructor() : NearbyPlaceMapper {

    override fun mapToNearbyPlaceItemList(
        placeList: List<Place>
    ): List<NearbyPlaceItem> {
        return placeList.map {
            mapToNearbyPlaceItem(place = it)
        }
    }

    override fun mapToNearbyPlaceItem(
        place: Place
    ): NearbyPlaceItem {
        return place.let {
            NearbyPlaceItem(
                placeId = it.id.id,
                placeName = it.name.name,
                placeAddress = it.address.detail.address,
                latitude = it.address.geocode.latitude.latitude,
                longitude = it.address.geocode.longitude.longitude,
                isFavored = mapToFavoredView(favoredStatus = it.placeFavoriteStatus)
            )
        }
    }

    private fun mapToFavoredView(
        favoredStatus: PlaceFavoriteStatus
    ): Boolean {
        return when (favoredStatus) {
            PlaceFavoriteStatus.Favored -> true
            PlaceFavoriteStatus.NotFavored -> false
        }
    }

}