package com.github.edasich.base.data.remote.parser.gson

import arrow.core.Either
import com.github.edasich.base.data.remote.JsonParser
import com.google.gson.Gson
import javax.inject.Inject

class GsonJsonParser @Inject constructor() : JsonParser {

    override fun <MODEL> serialize(model: MODEL): Either<Throwable, String> {
        return Either.catch {
            Gson().toJson(model)
        }
    }

    override fun <MODEL> deserialize(json: String, model: Class<MODEL>): Either<Throwable, MODEL> {
        return Either.catch {
            Gson().fromJson(json, model)
        }
    }

}