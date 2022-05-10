package com.github.edasich.base.data.remote.rest.di

import com.github.edasich.base.data.BuildConfig
import com.github.edasich.base.data.local.ApplicationSessionProvider
import com.github.edasich.base.data.local.RemoteConfigProvider
import com.github.edasich.base.data.remote.rest.AuthenticationInterceptor
import com.github.edasich.base.data.remote.rest.EitherApiExecutor
import com.github.edasich.base.data.remote.rest.EitherApiExecutorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RemoteRestModule {

    companion object {

        @Singleton
        @Provides
        fun provideAuthenticationInterceptor(
            applicationSessionProvider: ApplicationSessionProvider,
        ): AuthenticationInterceptor {
            return AuthenticationInterceptor(
                applicationSessionProvider = applicationSessionProvider,
            )
        }

        @Singleton
        @Provides
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        @AuthorizedOkHttp
        @Singleton
        @Provides
        fun provideAuthorizedOkHttpClient(
            authenticationInterceptor: AuthenticationInterceptor,
            httpLoggingInterceptor: HttpLoggingInterceptor
        ): OkHttpClient {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(authenticationInterceptor)
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(httpLoggingInterceptor)
            }
            builder.readTimeout(30, TimeUnit.SECONDS)
            builder.writeTimeout(30, TimeUnit.SECONDS)
            builder.callTimeout(30, TimeUnit.SECONDS)
            builder.connectTimeout(30, TimeUnit.SECONDS)
            return builder.build()
        }

        @UnauthorizedOkHttp
        @Singleton
        @Provides
        fun provideUnauthorizedOkHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor
        ): OkHttpClient {
            val builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(httpLoggingInterceptor)
            }
            builder.readTimeout(30, TimeUnit.SECONDS)
            builder.writeTimeout(30, TimeUnit.SECONDS)
            builder.callTimeout(30, TimeUnit.SECONDS)
            builder.connectTimeout(30, TimeUnit.SECONDS)
            return builder.build()
        }

        @AuthorizedRetrofit
        @Singleton
        @Provides
        fun provideAuthorizedRetrofit(
            @AuthorizedOkHttp
            okHttpClient: OkHttpClient,
            remoteConfigProviderImpl: RemoteConfigProvider
        ): Retrofit {
            return Retrofit
                .Builder()
                .baseUrl(remoteConfigProviderImpl.provideRestConfig().serverUri.toURL())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @UnauthorizedRetrofit
        @Singleton
        @Provides
        fun provideUnauthorizedRetrofit(
            @UnauthorizedOkHttp
            okHttpClient: OkHttpClient,
            remoteConfigProviderImpl: RemoteConfigProvider
        ): Retrofit {
            return Retrofit
                .Builder()
                .baseUrl(remoteConfigProviderImpl.provideRestConfig().serverUri.toURL())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    }

    @Singleton
    @Binds
    abstract fun bindEitherApiExecutor(
        impl: EitherApiExecutorImpl
    ): EitherApiExecutor

}

@Qualifier
@Retention(value = AnnotationRetention.BINARY)
internal annotation class AuthorizedOkHttp

@Qualifier
@Retention(value = AnnotationRetention.BINARY)
internal annotation class UnauthorizedOkHttp

@Qualifier
@Retention(value = AnnotationRetention.BINARY)
annotation class AuthorizedRetrofit

@Qualifier
@Retention(value = AnnotationRetention.BINARY)
annotation class UnauthorizedRetrofit
