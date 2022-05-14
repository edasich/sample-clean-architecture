package com.github.edasich.place.finder.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.edasich.place.finder.data.local.dao.PlaceDao
import com.github.edasich.place.finder.data.local.model.PlaceEntity

@Database(
    entities = [
        PlaceEntity::class
    ],
    version = 1
)
abstract class PlaceDatabase : RoomDatabase() {

    companion object {
        @Suppress("MemberVisibilityCanBePrivate")
        const val DATABASE_NAME = "place.db"

        fun buildDatabase(context: Context): PlaceDatabase {
            return Room
                .databaseBuilder(
                    context,
                    PlaceDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun placeDao(): PlaceDao

}