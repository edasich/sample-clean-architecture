package com.github.edasich.test.common.data.remote

import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun <API> createApi(
    mockWebServer: MockWebServer,
    apiClass: Class<API>
): API {
    return Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(apiClass)
}