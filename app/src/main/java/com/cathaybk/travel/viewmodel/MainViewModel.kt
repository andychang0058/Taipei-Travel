package com.cathaybk.travel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cathaybk.travel.manager.language.Language
import com.cathaybk.travel.manager.language.LanguageManager
import com.cathaybk.travel.manager.theme.ThemeManager
import com.cathaybk.travel.manager.theme.ThemeManagerImpl
import com.cathaybk.travel.storage.LanguageDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
class MainViewModel(
    private val themeManagerImpl: ThemeManagerImpl,
    private val languageManager: LanguageManager,
    private val languageDataStore: LanguageDataStore,
) : ViewModel(),
    ThemeManager by themeManagerImpl {

    private val _selectedLanguage = MutableStateFlow<Language?>(null)
    val selectedLanguage: StateFlow<Language?> = _selectedLanguage

    val displayLanguage: StateFlow<Language> = languageManager.displayLanguage

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val language = languageDataStore.getSelectedLanguage()
            _selectedLanguage.update { language }
        }
    }

    fun updateLanguage(language: Language?) {
        viewModelScope.launch(Dispatchers.IO) {
            languageDataStore.saveSelectedLanguage(language)
            _selectedLanguage.update { language }
            languageManager.updateDisplayLanguage(language)
        }
    }
}