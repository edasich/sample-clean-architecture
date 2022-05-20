package com.github.edasich.place.finder.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    val latLng: String,
    val nextKey: String?
)
