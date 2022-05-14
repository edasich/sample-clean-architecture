package com.github.edasich.place.finder.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * This entity is used for partial updates.
 */
@Entity
data class PlaceEntityWithoutFavored(
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "address")
    val address: String,
)