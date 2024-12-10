package com.cathaybk.travel.api

import kotlinx.serialization.json.Json

val json = Json {
    encodeDefaults = true
    explicitNulls = false
    isLenient = true
    ignoreUnknownKeys = true
    coerceInputValues = true
}