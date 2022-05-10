package com.github.edasich.place.finder.domain

sealed class PlaceFavoriteStatus {
    object Favored : PlaceFavoriteStatus()
    object NotFavored : PlaceFavoriteStatus()
}
