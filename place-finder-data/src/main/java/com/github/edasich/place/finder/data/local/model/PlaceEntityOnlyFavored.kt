package com.github.edasich.place.finder.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * This entity is used for partial updates.
 */
@Entity
data class PlaceEntityOnlyFavored(
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "favored")
    val isFavored: Boolean,
)