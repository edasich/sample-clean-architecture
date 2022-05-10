package com.github.edasich.base.service

import arrow.core.Either

interface EitherUseCase<PARAM, SUCCESS, REASON : UseCaseErrorReason> {

    suspend operator fun invoke(
        param: PARAM
    ): Either<GeneralUseCaseError<REASON>, SUCCESS>

}