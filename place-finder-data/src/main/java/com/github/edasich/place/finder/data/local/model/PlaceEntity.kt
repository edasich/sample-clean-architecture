package com.github.edasich.place.finder.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "place",
    indices = [
        Index(value = ["latitude","longitude"], unique = true)
    ]
)
data class PlaceEntity(
    @PrimaryKey(autoGenerate = false)
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
    @ColumnInfo(name = "favored", defaultValue = "0")
    val isFavored: Boolean,
)