package com.cathaybk.travel.manager.language

import kotlinx.coroutines.flow.StateFlow

interface LanguageManager {
    val displayLanguage: StateFlow<Language>
    val selectedLanguage: StateFlow<Language?>
    fun updateLanguage(language: Language?)
    fun onLanguageConfigChanged()
}