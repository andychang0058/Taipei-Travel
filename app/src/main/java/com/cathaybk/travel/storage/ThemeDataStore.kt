package com.cathaybk.travel.storage

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cathaybk.travel.extensions.dataStore
import com.cathaybk.travel.manager.theme.ThemeOption
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class ThemeDataStore(private val application: Application) {

    companion object {
        private val THEME_SETTING_KEY = stringPreferencesKey("theme_setting_key")
        private const val THEME_DARK_VALUE = "dark"
        private const val THEME_LIGHT_VALUE = "light"
        private const val THEME_SYSTEM_VALUE = "system"

    }

    suspend fun getTheme(): ThemeOption {
        return when (application.dataStore.data.map { it[THEME_SETTING_KEY] }.first()) {
            THEME_DARK_VALUE -> ThemeOption.Dark
            THEME_LIGHT_VALUE -> ThemeOption.Light
            THEME_SYSTEM_VALUE -> ThemeOption.System
            else -> ThemeOption.System
        }
    }

    suspend fun saveTheme(themeOption: ThemeOption) {
        application.dataStore.edit { preferences ->
            preferences[THEME_SETTING_KEY] = when (themeOption) {
                ThemeOption.Dark -> THEME_DARK_VALUE
                ThemeOption.Light -> THEME_LIGHT_VALUE
                ThemeOption.System -> THEME_SYSTEM_VALUE
            }
        }
    }
}