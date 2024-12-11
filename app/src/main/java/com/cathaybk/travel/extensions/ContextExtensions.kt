package com.cathaybk.travel.extensions

import android.content.Context
import android.content.res.Configuration
import android.icu.util.ULocale
import androidx.datastore.preferences.preferencesDataStore
import com.cathaybk.travel.manager.language.Language
import java.util.Locale

val Context.dataStore by preferencesDataStore(name = "settings")

fun Context.wrapWithLocale(langTag: String): Context {
    val locale = Locale.forLanguageTag(langTag)
    val config = Configuration(resources.configuration).apply {
        setLocale(locale)
    }
    return createConfigurationContext(config)
}

fun Context.findSystemBestMatchLanguage(): Language {
    val systemLocales = resources.configuration.locales
    for (i in 0 until systemLocales.size()) {
        val systemLocaleScript =
            ULocale.addLikelySubtags(ULocale.forLocale(systemLocales[i])).script
        Language.getAllSupportLanguage().forEach {
            val script = ULocale.addLikelySubtags(ULocale.forLanguageTag(it.tag)).script
            if (script == systemLocaleScript) return it
        }
    }
    return Language.default
}