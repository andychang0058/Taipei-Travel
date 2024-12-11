package com.cathaybk.travel.viewmodel

import androidx.lifecycle.ViewModel
import com.cathaybk.travel.manager.theme.ThemeManager
import com.cathaybk.travel.manager.theme.ThemeManagerImpl
import org.koin.core.annotation.Factory

@Factory
class MainViewModel(
    private val themeManagerImpl: ThemeManagerImpl,
) : ViewModel(),
    ThemeManager by themeManagerImpl