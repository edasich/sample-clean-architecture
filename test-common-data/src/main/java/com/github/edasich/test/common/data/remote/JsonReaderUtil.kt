package com.github.edasich.test.common.data.remote

import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets

fun readJsonFileWithNoNewLineOrWhiteSpace(
    classLoader: ClassLoader,
    fileName: String
): String? {

    val inputStream =
        classLoader.getResourceAsStream("api-response/$fileName")
    val source =
        inputStream?.let {
            inputStream.source().buffer()
        }
    return source
        ?.readString(charset = StandardCharsets.UTF_8)
        ?.replace("\n", "")
        ?.replace(" ", "")

}