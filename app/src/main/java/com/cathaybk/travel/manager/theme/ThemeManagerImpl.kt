package com.cathaybk.travel.manager.theme

import android.app.Application
import android.content.res.Configuration
import com.cathaybk.travel.storage.ThemeDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class ThemeManagerImpl(
    private val application: Application,
    private val themeDataStore: ThemeDataStore,
) : ThemeManager {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _isDarkTheme = MutableStateFlow<Boolean>(false)
    override val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    private val _currentTheme = MutableStateFlow<ThemeOption>(ThemeOption.System)
    override val currentTheme: StateFlow<ThemeOption> = _currentTheme

    init {
        onThemeConfigChanged()
    }

    override fun updateTheme(themeOption: ThemeOption) {
        scope.launch {
            themeDataStore.saveTheme(themeOption)
            notifyThemeChanged(themeOption)
        }
    }

    override fun onThemeConfigChanged() {
        scope.launch {
            notifyThemeChanged(themeDataStore.getTheme())
        }
    }

    private fun notifyThemeChanged(themeOption: ThemeOption) {
        _currentTheme.update { themeOption }
        _isDarkTheme.update {
            when (themeOption) {
                ThemeOption.Dark -> true
                ThemeOption.Light -> false
                ThemeOption.System -> isSystemDarkTheme()
            }
        }
    }

    private fun isSystemDarkTheme(): Boolean {
        return when (application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }
    }
}