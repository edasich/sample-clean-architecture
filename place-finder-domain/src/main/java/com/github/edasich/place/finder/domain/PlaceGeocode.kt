package com.github.edasich.place.finder.domain

import com.github.edasich.location.domain.Latitude
import com.github.edasich.location.domain.Longitude

data class PlaceGeocode(
    val latitude : Latitude,
    val longitude: Longitude
)