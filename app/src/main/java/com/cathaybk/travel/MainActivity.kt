package com.cathaybk.travel

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cathaybk.travel.extensions.wrapWithLocale
import com.cathaybk.travel.ui.TravelApp
import com.cathaybk.travel.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by mainViewModel.isDarkTheme.collectAsStateWithLifecycle()
            LaunchedEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT
                    ) { isDarkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        // The default color defined by EdgeToEdge.kt
                        Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
                        Color.argb(0x80, 0x1b, 0x1b, 0x1b),
                    ) { isDarkTheme }
                )
            }

            val displayLanguage by mainViewModel.displayLanguage.collectAsStateWithLifecycle()
            val selectedLanguage by mainViewModel.selectedLanguage.collectAsStateWithLifecycle()
            val context = LocalContext.current
            val languageContext by remember(selectedLanguage) {
                derivedStateOf {
                    selectedLanguage?.run { context.wrapWithLocale(displayLanguage.tag) } ?: context
                }
            }
            CompositionLocalProvider(LocalContext provides languageContext) {
                TravelApp(mainViewModel = mainViewModel)
            }
        }
    }
}
