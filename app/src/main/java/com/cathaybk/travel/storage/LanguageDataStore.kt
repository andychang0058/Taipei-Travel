package com.cathaybk.travel.storage

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cathaybk.travel.extensions.dataStore
import com.cathaybk.travel.manager.language.Language
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class LanguageDataStore(private val application: Application) {

    companion object {
        private val LANGUAGE_SETTING_KEY = stringPreferencesKey("language_setting_key")
    }

    suspend fun getSelectedLanguage(): Language? {
        val langTag = application.dataStore.data.map { it[LANGUAGE_SETTING_KEY] }.first()
        return Language.fromTag(langTag)
    }

    suspend fun saveSelectedLanguage(langOption: Language?) {
        application.dataStore.edit { preferences ->
            if (langOption == null) {
                preferences.remove(LANGUAGE_SETTING_KEY)
            } else {
                preferences[LANGUAGE_SETTING_KEY] = langOption.tag
            }
        }
    }
}