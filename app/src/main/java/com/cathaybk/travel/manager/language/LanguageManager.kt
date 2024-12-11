package com.cathaybk.travel.manager.language

import kotlinx.coroutines.flow.StateFlow

interface LanguageManager {
    val displayLanguage: StateFlow<Language>
    fun updateDisplayLanguage(language: Language?)
}