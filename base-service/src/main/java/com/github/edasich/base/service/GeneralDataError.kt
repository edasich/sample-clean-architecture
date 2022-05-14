package com.github.edasich.base.service

sealed class GeneralDataError {

    sealed class GeneralRemoteError : GeneralDataError() {
        object RequestCanceled : GeneralRemoteError()

        object NetworkConnection : GeneralRemoteError()

        data class ServerError(
            val httpErrorCode: Int
        ) : GeneralRemoteError()

        data class ServerWithCustomError(
            val httpErrorCode: Int,
            val code: Int,
            val message: String
        ) : GeneralRemoteError()

        object Unknown : GeneralRemoteError()
    }

    sealed class GeneralLocalError : GeneralDataError() {
        object NoMoreData : GeneralDataError()
    }

}