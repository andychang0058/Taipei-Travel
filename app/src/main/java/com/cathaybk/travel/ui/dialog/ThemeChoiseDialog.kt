package com.cathaybk.travel.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.cathaybk.travel.R
import com.cathaybk.travel.manager.theme.ThemeOption
import com.cathaybk.travel.ui.base.theme.TravelTheme

@Composable
fun ThemeChoiceDialog(
    currentTheme: ThemeOption,
    onThemeSelected: (ThemeOption) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.select_theme))
        },
        text = {
            Column {
                DialogRadioOption(
                    text = stringResource(R.string.theme_system),
                    isSelected = currentTheme == ThemeOption.System,
                    onSelected = { onThemeSelected(ThemeOption.System) }
                )
                DialogRadioOption(
                    text = stringResource(R.string.theme_dark),
                    isSelected = currentTheme == ThemeOption.Dark,
                    onSelected = { onThemeSelected(ThemeOption.Dark) }
                )
                DialogRadioOption(
                    text = stringResource(R.string.theme_light),
                    isSelected = currentTheme == ThemeOption.Light,
                    onSelected = { onThemeSelected(ThemeOption.Light) }
                )
            }
        },
        confirmButton = {}
    )
}

@PreviewLightDark
@Composable
fun ThemeChoiceDialogPreview() {
    TravelTheme {
        ThemeChoiceDialog(
            currentTheme = ThemeOption.System,
            onThemeSelected = {},
            onDismiss = {}
        )
    }
}