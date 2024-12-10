package com.cathaybk.travel.extensions

import okhttp3.ResponseBody
import java.io.IOException

fun ResponseBody?.cloneString(): String? {
    if (this == null) return null
    return try {
        source().run {
            request(Long.MAX_VALUE) // Buffer the entire body.
            buffer.clone().readString(Charsets.UTF_8)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}