package com.cathaybk.travel.ui.attraction

import android.os.Bundle
import androidx.navigation.NavType
import com.cathaybk.travel.extensions.getParcelableCompat
import com.cathaybk.travel.model.Attraction
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

class AttractionDataNavType : NavType<Attraction>(isNullableAllowed = false) {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    override fun get(bundle: Bundle, key: String): Attraction? {
        return bundle.getParcelableCompat<Attraction>(key)
    }

    override fun parseValue(value: String): Attraction {
        val jsonString = URLDecoder.decode(value, "UTF-8")
        return json.decodeFromString(jsonString)
    }

    override fun serializeAsValue(value: Attraction): String {
        val jsonString = Json.encodeToString(value)
        return URLEncoder.encode(jsonString, "UTF-8")
    }

    override fun put(bundle: Bundle, key: String, value: Attraction) {
        bundle.putParcelable(key, value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        return true
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}