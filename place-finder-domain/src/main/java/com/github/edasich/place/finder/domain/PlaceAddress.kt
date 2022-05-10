package com.github.edasich.place.finder.domain

data class PlaceAddress(
    val geocode: PlaceGeocode,
    val detail: PlaceAddressDetail
)