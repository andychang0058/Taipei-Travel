package com.cathaybk.travel.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cathaybk.travel.R
import com.cathaybk.travel.model.Attraction
import com.cathaybk.travel.ui.attraction.AttractionDataNavType
import com.cathaybk.travel.ui.attraction.AttractionScreen
import com.cathaybk.travel.ui.base.theme.TravelTheme
import com.cathaybk.travel.ui.dialog.LanguageChoiceDialog
import com.cathaybk.travel.ui.dialog.ThemeChoiceDialog
import com.cathaybk.travel.ui.home.AppToolbar
import com.cathaybk.travel.ui.home.HomeScreen
import com.cathaybk.travel.ui.news.NewsScreen
import com.cathaybk.travel.ui.webview.WebViewScreen
import com.cathaybk.travel.utils.Utils
import com.cathaybk.travel.viewmodel.MainViewModel
import kotlin.reflect.typeOf

@Composable
fun TravelApp(mainViewModel: MainViewModel) {
    val isDarkTheme by mainViewModel.isDarkTheme.collectAsStateWithLifecycle()
    val currentTheme by mainViewModel.currentTheme.collectAsStateWithLifecycle()
    val selectedLanguage by mainViewModel.selectedLanguage.collectAsStateWithLifecycle()

    TravelTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()
        var showSelectLanguageDialog by remember { mutableStateOf(false) }
        var showSelectThemeDialog by remember { mutableStateOf(false) }

        val backStackEntryState by navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntryState?.destination

        var customAppBarTitle by rememberSaveable { mutableStateOf("") }

        val showNavigation by remember(currentDestination) {
            derivedStateOf {
                currentDestination?.hasRoute<Screen.Home>() == false
            }
        }
        val showMoreSettings by remember(currentDestination) {
            derivedStateOf {
                currentDestination?.hasRoute<Screen.Home>() == true
            }
        }

        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    val title = when {
                        currentDestination?.hasRoute<Screen.Home>() == true -> stringResource(R.string.app_name)
                        currentDestination?.hasRoute<Screen.News>() == true -> stringResource(R.string.news)
                        else -> customAppBarTitle
                    }
                    AppToolbar(
                        title = title,
                        showNavigation = showNavigation,
                        showMoreSettings = showMoreSettings,
                        onSelectLanguageClick = { showSelectLanguageDialog = true },
                        onSelectThemeClick = { showSelectThemeDialog = true },
                        onNavigationClicked = { navController.navigateUp() },
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<Screen.Home>(
                        exitTransition = null,
                        popEnterTransition = null,
                    ) {
                        HomeScreen(
                            onMoreNewsClicked = {
                                navController.navigate(Screen.News)
                            },
                            onNewsClicked = {
                                it.url?.let { url -> navController.navigate(Screen.Web(url)) }
                            },
                            onAttractionClicked = { attraction ->
                                customAppBarTitle = attraction.name.orEmpty()
                                navController.navigate(Screen.AttractionDetail(attraction))
                            }
                        )
                    }

                    composable<Screen.News>(
                        enterTransition = { enterTransition },
                        exitTransition = null,
                        popEnterTransition = null,
                        popExitTransition = { exitTransition }
                    ) {
                        NewsScreen(
                            onNewsClicked = {
                                it.url?.let { url -> navController.navigate(Screen.Web(url)) }
                            }
                        )
                    }

                    composable<Screen.Web>(
                        enterTransition = null,
                        exitTransition = null,
                        popEnterTransition = null,
                        popExitTransition = { exitTransition }
                    ) { backStackEntry ->
                        WebViewScreen(
                            url = backStackEntry.toRoute<Screen.Web>().url,
                            onTitleChanged = { title -> customAppBarTitle = title }
                        )
                    }

                    composable<Screen.AttractionDetail>(
                        typeMap = mapOf(
                            typeOf<Attraction>() to AttractionDataNavType()
                        ),
                        enterTransition = { enterTransition },
                        exitTransition = null,
                        popEnterTransition = null,
                        popExitTransition = { exitTransition },
                    ) { backStackEntry ->
                        AttractionScreen(
                            attraction = backStackEntry.toRoute<Screen.AttractionDetail>().attraction,
                            onOfficialSiteClicked = { url -> navController.navigate(Screen.Web(url)) },
                            onLocationClicked = { lat, lng, address ->
                                Utils.openGoogleMapWithAddress(
                                    context = context,
                                    lat = lat,
                                    lng = lng,
                                    address = address,
                                )
                            }
                        )
                    }
                }
            }
        }

        if (showSelectLanguageDialog) {
            LanguageChoiceDialog(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = {
                    mainViewModel.updateLanguage(it)
                    showSelectLanguageDialog = false
                },
                onDismiss = { showSelectLanguageDialog = false }
            )
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

private val enterTransition = slideInHorizontally(
    initialOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(300)
)

private val exitTransition = slideOutHorizontally(
    targetOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(300)
)