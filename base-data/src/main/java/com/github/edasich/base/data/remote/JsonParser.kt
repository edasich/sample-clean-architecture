package com.github.edasich.base.data.remote

import arrow.core.Either

interface JsonParser {
    fun <MODEL> serialize(model: MODEL): Either<Throwable, String>
    fun <MODEL> deserialize(json: String, model: Class<MODEL>): Either<Throwable, MODEL>
}