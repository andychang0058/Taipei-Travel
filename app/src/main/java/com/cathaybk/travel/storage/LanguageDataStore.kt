package com.cathaybk.travel.storage

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cathaybk.travel.extensions.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class LanguageDataStore(private val application: Application) {

    companion object {
        private val LANGUAGE_SETTING_KEY = stringPreferencesKey("language_setting_key")
    }

    suspend fun getLanguage(): String {
        return application.dataStore.data.map { it[LANGUAGE_SETTING_KEY] }.first() ?: "en"
    }

    suspend fun saveLanguage(lang: String) {
        application.dataStore.edit { preferences ->
            preferences[LANGUAGE_SETTING_KEY] = lang
        }
    }
}