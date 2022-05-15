package com.github.edasich.test.common.data.remote

import arrow.core.Either
import com.github.edasich.base.data.remote.rest.EitherApiExecutor
import com.github.edasich.base.data.remote.rest.RemoteError
import retrofit2.Response

class FakeEitherApiExecutorImpl : EitherApiExecutor {

    override suspend fun <R> executeWithPayload(
        requestToCall: suspend () -> Response<R>
    ): Either<RemoteError, R> {
        return executeWithResponse(requestToCall).map {
            it.body()!!
        }
    }

    override suspend fun <R> executeWithResponse(
        requestToCall: suspend () -> Response<R>
    ): Either<RemoteError, Response<R>> {
        val response = requestToCall.invoke()
        return if (response.isSuccessful) {
            Either.Right(response)
        } else {
            Either.Left(RemoteError.Unknown)
        }
    }

}