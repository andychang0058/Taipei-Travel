package com.cathaybk.travel.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cathaybk.travel.ui.dialog.ThemeChoiceDialog
import com.cathaybk.travel.ui.home.AppToolbar
import com.cathaybk.travel.ui.home.HomeScreen
import com.cathaybk.travel.ui.theme.TravelTheme
import com.cathaybk.travel.viewmodel.MainViewModel

@Composable
fun TravelApp(mainViewModel: MainViewModel) {
    val isDarkTheme by mainViewModel.isDarkTheme.collectAsStateWithLifecycle()
    val currentTheme by mainViewModel.currentTheme.collectAsStateWithLifecycle()

    TravelTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()
        var showSelectLanguageDialog by remember { mutableStateOf(false) }
        var showSelectThemeDialog by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    AppToolbar(
                        navController = navController,
                        onSelectLanguageClick = { showSelectLanguageDialog = true },
                        onSelectThemeClick = { showSelectThemeDialog = true }
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<Screen.Home> { HomeScreen() }
                }
            }
        }

        if (showSelectLanguageDialog) {
            // SelectLanguageDialog(onDismiss = { showSelectLanguageDialog = false })
        }
        if (showSelectThemeDialog) {
            ThemeChoiceDialog(
                currentTheme = currentTheme,
                onThemeSelected = {
                    mainViewModel.updateTheme(it)
                    showSelectThemeDialog = false
                },
                onDismiss = { showSelectThemeDialog = false }
            )
        }
    }
}