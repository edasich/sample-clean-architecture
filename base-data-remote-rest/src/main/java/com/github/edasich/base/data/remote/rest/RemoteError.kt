package com.github.edasich.base.data.remote.rest

import com.github.edasich.base.service.GeneralDataError
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Response

sealed class RemoteError {
    object RequestCanceled : RemoteError()

    object NetworkConnection : RemoteError()

    data class ServerError(
        val httpErrorCode: Int
    ) : RemoteError()

    data class ServerWithCustomError(
        val httpErrorCode: Int,
        val serverCustomError: ServerCustomError
    ) : RemoteError()

    object Unknown : RemoteError()
}

data class ServerCustomError(
    @SerializedName("error_code")
    val code: Int = 0,
    @SerializedName("message")
    val message: String = ""
)

fun RemoteError.toGeneralDataError(): GeneralDataError {
    return when (this) {
        RemoteError.RequestCanceled -> GeneralDataError.GeneralRemoteError.RequestCanceled
        RemoteError.NetworkConnection -> GeneralDataError.GeneralRemoteError.NetworkConnection
        is RemoteError.ServerError -> GeneralDataError.GeneralRemoteError.ServerError(
            httpErrorCode = this.httpErrorCode
        )
        is RemoteError.ServerWithCustomError -> GeneralDataError.GeneralRemoteError.ServerWithCustomError(
            httpErrorCode = this.httpErrorCode,
            code = this.serverCustomError.code,
            message = this.serverCustomError.message
        )
        RemoteError.Unknown -> GeneralDataError.GeneralRemoteError.Unknown
    }
}