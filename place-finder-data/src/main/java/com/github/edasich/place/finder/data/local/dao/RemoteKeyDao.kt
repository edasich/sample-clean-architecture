package com.github.edasich.place.finder.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.edasich.place.finder.data.local.model.RemoteKey

@Dao
interface RemoteKeyDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrReplace(remoteKey: RemoteKey)

  @Query("SELECT * FROM remote_keys WHERE latLng = :query")
  suspend fun remoteKeyByQuery(query: String): RemoteKey

  @Query("DELETE FROM remote_keys WHERE latLng = :query")
  suspend fun deleteByQuery(query: String)

}
