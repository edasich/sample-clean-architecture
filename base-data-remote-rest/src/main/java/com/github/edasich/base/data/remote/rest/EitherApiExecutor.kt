package com.github.edasich.base.data.remote.rest

import arrow.core.Either
import retrofit2.Response

interface EitherApiExecutor {

    suspend fun <R> execute(
        requestToCall: suspend () -> Response<R>,
    ): Either<RemoteError, R>

}