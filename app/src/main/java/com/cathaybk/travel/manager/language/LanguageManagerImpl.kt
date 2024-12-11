package com.cathaybk.travel.manager.language

import android.app.Application
import com.cathaybk.travel.extensions.findSystemBestMatchLanguage
import com.cathaybk.travel.storage.LanguageDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
class LanguageManagerImpl(
    private val application: Application,
    private val languageDataStore: LanguageDataStore,
) : LanguageManager {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _displayLanguage = MutableStateFlow<Language>(Language.default)
    override val displayLanguage: StateFlow<Language> = _displayLanguage

    private val _selectedLanguage = MutableStateFlow<Language?>(null)
    override val selectedLanguage: StateFlow<Language?> = _selectedLanguage

    init {
        onLanguageConfigChanged()
    }

    override fun updateLanguage(language: Language?) {
        scope.launch(Dispatchers.IO) {
            languageDataStore.saveSelectedLanguage(language)
            _selectedLanguage.update { language }
            notifyLanguageUpdated(language)
        }
    }

    override fun onLanguageConfigChanged() {
        scope.launch {
            val language = withContext(Dispatchers.IO) { languageDataStore.getSelectedLanguage() }
            notifyLanguageUpdated(language)
        }
    }

    private fun notifyLanguageUpdated(language: Language?) {
        _displayLanguage.update { language ?: application.findSystemBestMatchLanguage() }
    }
}