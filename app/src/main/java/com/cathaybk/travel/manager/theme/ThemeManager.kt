package com.cathaybk.travel.manager.theme

import kotlinx.coroutines.flow.StateFlow

interface ThemeManager {
    val isDarkTheme: StateFlow<Boolean>
    val currentTheme: StateFlow<ThemeOption>
    fun updateTheme(themeOption: ThemeOption)
    fun onThemeConfigChanged()
}