package com.cathaybk.travel.viewmodel

import androidx.lifecycle.ViewModel
import com.cathaybk.travel.manager.language.LanguageManager
import com.cathaybk.travel.manager.language.LanguageManagerImpl
import com.cathaybk.travel.manager.theme.ThemeManager
import com.cathaybk.travel.manager.theme.ThemeManagerImpl
import org.koin.core.annotation.Factory

@Factory
class MainViewModel(
    private val themeManagerImpl: ThemeManagerImpl,
    private val languageManagerImpl: LanguageManagerImpl,
) : ViewModel(),
    ThemeManager by themeManagerImpl,
    LanguageManager by languageManagerImpl