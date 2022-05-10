package com.github.edasich.base.service

import arrow.core.Either

sealed class GeneralUseCaseError<out REASON> {

    data class UseCaseError<out REASON : UseCaseErrorReason>(
        val reason: REASON
    ) : GeneralUseCaseError<REASON>()

    data class DataError(
        val reason: GeneralDataError
    ) : GeneralUseCaseError<Nothing>()

}

interface UseCaseErrorReason

fun <ERROR : UseCaseErrorReason> ERROR.toGeneralUseCaseError(): GeneralUseCaseError<ERROR> {
    return GeneralUseCaseError.UseCaseError(reason = this)
}

fun <ERROR> GeneralDataError.toGeneralUseCaseError(): GeneralUseCaseError<ERROR> {
    return GeneralUseCaseError.DataError(reason = this)
}

fun GeneralUseCaseError<*>.hasUseCaseError(): Boolean {
    return this is GeneralUseCaseError.UseCaseError
}

fun GeneralUseCaseError<*>.hasDataError(): Boolean {
    return this is GeneralUseCaseError.DataError
}

fun Either<GeneralUseCaseError<*>, *>.hasUseCaseError(): Boolean {
    return when (this) {
        is Either.Right -> false
        is Either.Left -> this.value.hasUseCaseError()
    }
}

fun Either<GeneralUseCaseError<*>, *>.hasDataError(): Boolean {
    return when (this) {
        is Either.Right -> false
        is Either.Left -> this.value.hasDataError()
    }
}