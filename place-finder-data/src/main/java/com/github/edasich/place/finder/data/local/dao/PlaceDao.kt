package com.github.edasich.place.finder.data.local.dao

import androidx.room.*
import com.github.edasich.base.data.local.BaseDao
import com.github.edasich.place.finder.data.local.model.PlaceEntity
import com.github.edasich.place.finder.data.local.model.PlaceEntityWithoutFavored
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PlaceDao : BaseDao<PlaceEntity>() {

    @Query(
        """
            select * from place 
            where latitude > :pointSouthX and latitude < :pointNorthX and longitude < :pointEastY and longitude > :pointWestY
            limit :limit offset :page - 1
            """
    )
    abstract fun getPlaces(
        pointSouthX: Float,
        pointNorthX: Float,
        pointEastY: Float,
        pointWestY: Float,
        limit: Int,
        page: Int,
    ): Flow<List<PlaceEntity>>

    @Query("select * from place where id = :placeId limit 1")
    abstract suspend fun getPlace(placeId: String): PlaceEntity?

    @Insert(
        onConflict = OnConflictStrategy.IGNORE,
        entity = PlaceEntity::class
    )
    abstract suspend fun insertPlaces(
        entities: List<PlaceEntityWithoutFavored>
    ): List<Long>

    @Update(
        entity = PlaceEntity::class
    )
    abstract suspend fun updatePlaces(
        entities: List<PlaceEntityWithoutFavored>
    )

    @Transaction
    open suspend fun upsertPlacesWithoutFavored(entities: List<PlaceEntityWithoutFavored>) {
        val insertResult = insertPlaces(entities = entities)
        val updateList: MutableList<PlaceEntityWithoutFavored> = ArrayList()
        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(element = entities[i])
            }
        }
        if (updateList.isNotEmpty()) {
            updatePlaces(entities = updateList)
        }
    }

    @Query("select * from place where favored = 1")
    abstract fun getFavoritePlaces(): Flow<List<PlaceEntity>>

}