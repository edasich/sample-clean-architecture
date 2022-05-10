package com.github.edasich.base.data.remote.rest

import com.github.edasich.base.data.local.ApplicationSessionProvider
import com.github.edasich.base.data.local.model.ApplicationSession
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val applicationSessionProvider: ApplicationSessionProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return when (val session = applicationSessionProvider.provideSession()) {
            is ApplicationSession.Authorized -> {
                val originalRequest = chain.request()
                if (originalRequest.headers["Authorization"] == null) {
                    val authenticatedRequest =
                        chain.createAuthenticatedRequest(token = session.sessionCredential.restAccessToken.token)
                    chain.proceed(request = authenticatedRequest)
                } else {
                    chain.proceed(request = originalRequest)
                }
            }
            ApplicationSession.Unauthorized -> chain.proceed(request = chain.request())
        }
    }

    private fun Interceptor.Chain.createAuthenticatedRequest(token: String): Request {
        return request()
            .newBuilder()
            .addHeader("Authorization", token)
            .build()
    }

}