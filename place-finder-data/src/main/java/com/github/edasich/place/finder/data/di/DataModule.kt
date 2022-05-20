package com.github.edasich.place.finder.data.di

import android.content.Context
import com.github.edasich.base.data.remote.rest.di.AuthorizedRetrofit
import com.github.edasich.place.finder.data.PlaceRepository
import com.github.edasich.place.finder.data.PlaceRepositoryImpl
import com.github.edasich.place.finder.data.local.PlaceLocalDataSource
import com.github.edasich.place.finder.data.local.PlaceLocalDataSourceImpl
import com.github.edasich.place.finder.data.local.dao.PlaceDao
import com.github.edasich.place.finder.data.local.dao.RemoteKeyDao
import com.github.edasich.place.finder.data.local.database.PlaceDatabase
import com.github.edasich.place.finder.data.mapper.PlaceMapper
import com.github.edasich.place.finder.data.mapper.internal.PlaceMapperImpl
import com.github.edasich.place.finder.data.remote.PlaceRemoteDataSource
import com.github.edasich.place.finder.data.remote.PlaceRemoteDataSourceImpl
import com.github.edasich.place.finder.data.remote.rest.PlaceRestService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

    companion object {

        @Singleton
        @Provides
        fun providePlaceDatabase(
            @ApplicationContext
            context: Context
        ): PlaceDatabase {
            return PlaceDatabase.buildDatabase(context = context)
        }

        @Singleton
        @Provides
        fun providePlaceDao(
            placeDatabase: PlaceDatabase
        ): PlaceDao {
            return placeDatabase.placeDao()
        }

        @Singleton
        @Provides
        fun provideRemoteKeyDao(
            placeDatabase: PlaceDatabase
        ): RemoteKeyDao {
            return placeDatabase.remoteKeyDao()
        }

        @Singleton
        @Provides
        fun providePlaceRestService(
            @AuthorizedRetrofit
            retrofit: Retrofit
        ): PlaceRestService {
            return retrofit.create(PlaceRestService::class.java)
        }

        @Singleton
        @Provides
        fun providePlaceMapper(): PlaceMapper {
            return PlaceMapperImpl(coroutineContext = Dispatchers.Default)
        }

    }

    @Singleton
    @Binds
    abstract fun bindPlaceRepository(
        impl: PlaceRepositoryImpl
    ): PlaceRepository

    @Singleton
    @Binds
    abstract fun bindPlaceLocalDataSource(
        impl: PlaceLocalDataSourceImpl
    ): PlaceLocalDataSource

    @Singleton
    @Binds
    abstract fun bindPlaceRemoteDataSource(
        impl: PlaceRemoteDataSourceImpl
    ): PlaceRemoteDataSource

}