package com.github.edasich.base.data.remote.rest

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import com.github.edasich.base.data.remote.JsonParser
import com.github.edasich.base.data.remote.parser.gson.di.GsonParser
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class EitherApiExecutorImpl @Inject constructor(
    @GsonParser
    private val jsonParser: JsonParser
) : EitherApiExecutor {

    override suspend fun <R> execute(
        requestToCall: suspend () -> Response<R>
    ): Either<RemoteError, R> {
        return try {
            val serverResponse = requestToCall.invoke()
            if (serverResponse.isSuccessful) {
                return Either.Right(value = serverResponse.body()!!)
            } else {
                val errorBody = serverResponse.errorBody() ?: return Either.Left(
                    value = RemoteError.ServerError(
                        httpErrorCode = serverResponse.code(),
                    )
                )
                return readServerErrorFromBody(
                    errorBody = errorBody
                ).flatMap { serverError ->
                    parseServerError(serverError = serverError)
                }.fold(
                    {
                        // failed to read or parse server error body
                        it.printStackTrace()
                        RemoteError.Unknown.left()
                    },
                    {
                        RemoteError.ServerWithCustomError(
                            httpErrorCode = serverResponse.code(),
                            serverCustomError = it
                        ).left()
                    }
                )
            }
        } catch (e: CancellationException) {
            e.printStackTrace()
            Either.Left(value = RemoteError.RequestCanceled)
        } catch (e: IOException) {
            e.printStackTrace()
            Either.Left(value = RemoteError.NetworkConnection)
        } catch (e: Exception) {
            e.printStackTrace()
            Either.Left(value = RemoteError.Unknown)
        }
    }

    private fun readServerErrorFromBody(errorBody: ResponseBody): Either<Throwable, String> {
        return Either.catch {
            errorBody.charStream().readText()
        }
    }

    private fun parseServerError(serverError: String): Either<Throwable, ServerCustomError> {
        return jsonParser.deserialize(json = serverError, ServerCustomError::class.java)
    }

}