package com.cathaybk.travel.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.cathaybk.travel.R
import com.cathaybk.travel.ui.theme.TravelTheme

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    title: String,
    showNavigation: Boolean = false,
    showMoreSettings: Boolean = true,
    onSelectLanguageClick: () -> Unit = {},
    onSelectThemeClick: () -> Unit = {},
    onNavigationClicked: () -> Unit = {},
) {
    var showMenu by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        windowInsets = TopAppBarDefaults.windowInsets,
        title = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = title
            )
        },
        navigationIcon = {
            if (showNavigation) {
                IconButton(onClick = { onNavigationClicked() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (showMoreSettings.not()) {
                return@CenterAlignedTopAppBar
            }
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        showMenu = false
                        onSelectLanguageClick()
                    },
                    text = { MenuText(stringResource(R.string.select_language)) }
                )
                DropdownMenuItem(
                    onClick = {
                        showMenu = false
                        onSelectThemeClick()
                    },
                    text = { MenuText(stringResource(R.string.select_theme)) }
                )
            }
        }
    )
}

@Composable
fun MenuText(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start,
        text = text
    )
}

@PreviewLightDark
@Composable
fun HomeToolbarPreview() {
    TravelTheme {
        Surface {
            AppToolbar(
                title = stringResource(R.string.app_name),
                showMoreSettings = true,
                showNavigation = true,
            )
        }
    }
}