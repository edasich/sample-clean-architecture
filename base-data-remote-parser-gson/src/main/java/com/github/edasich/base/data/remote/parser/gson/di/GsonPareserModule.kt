package com.github.edasich.base.data.remote.parser.gson.di

import com.github.edasich.base.data.remote.JsonParser
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.github.edasich.base.data.remote.parser.gson.GsonJsonParser
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class Module {

    @GsonParser
    @Singleton
    @Binds
    abstract fun bindGsonJsonParser(
        impl: GsonJsonParser
    ): JsonParser

}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GsonParser