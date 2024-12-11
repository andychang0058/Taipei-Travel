package com.cathaybk.travel.manager.theme

sealed class ThemeOption {
    data object Dark : ThemeOption()
    data object Light : ThemeOption()
    data object System : ThemeOption()
}