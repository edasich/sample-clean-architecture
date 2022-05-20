package com.github.edasich.place.finder.data.paged

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.edasich.place.finder.data.remote.PlaceRemoteDataSource
import com.github.edasich.place.finder.data.remote.rest.model.NearbyPlacesApiRequest
import com.github.edasich.place.finder.data.remote.rest.model.NextNearbyPlacesApiRequest
import com.github.edasich.place.finder.data.remote.rest.model.PlaceApiResponse

class NearbyPlacePagingSource constructor(
    private val remoteDataSource: PlaceRemoteDataSource
) : PagingSource<String, PlaceApiResponse>() {

    val TAG = "NearbyPlacePagingSource"

    init {
        Log.i(TAG, "INIT ...")
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, PlaceApiResponse> {
        val currentKey = params.key
        return if (currentKey == null) {
            Log.i(TAG, "Load : KEY IS NULL")
            val apiRequest = NearbyPlacesApiRequest(
                latLng = "35.631108,51.371594",
                radius = 1000
            )
            remoteDataSource.fetchNearbyPlaces(apiRequest)
                .fold(
                    ifLeft = {
                        LoadResult.Error(IllegalArgumentException())
                    },
                    ifRight = {
                        Log.i(TAG, "Load : Passed the key to NEXT ${it.nextNearbyPlacesLink}")
                        LoadResult.Page(
                            data = it.places,
                            prevKey = null,
                            nextKey = it.nextNearbyPlacesLink
                        )
                    }
                )
        } else {
            Log.i(TAG, "Load : KEY IS NOT NULL : $currentKey")
            val apiRequest = NextNearbyPlacesApiRequest(
                nextNearbyPlacesLink = currentKey
            )
            remoteDataSource.fetchNextNearbyPlaces(apiRequest)
                .fold(
                    ifLeft = {
                        LoadResult.Error(IllegalArgumentException())
                    },
                    ifRight = {
                        Log.i(TAG, "Load : Passed the key to NEXT ${it.nextNearbyPlacesLink}")
                        LoadResult.Page(
                            data = it.places,
                            prevKey = null,
                            nextKey = it.nextNearbyPlacesLink
                        )
                    }
                )
        }
    }

    override fun getRefreshKey(state: PagingState<String, PlaceApiResponse>): String? {
        Log.i(TAG, "Refresh ...")
        return null
    }

}