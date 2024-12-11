package com.cathaybk.travel.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.cathaybk.travel.R
import com.cathaybk.travel.manager.theme.ThemeOption
import com.cathaybk.travel.ui.theme.TravelTheme

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
                ThemeRadioButton(
                    text = stringResource(R.string.theme_system),
                    isSelected = currentTheme == ThemeOption.System,
                    onSelected = { onThemeSelected(ThemeOption.System) }
                )
                ThemeRadioButton(
                    text = stringResource(R.string.theme_dark),
                    isSelected = currentTheme == ThemeOption.Dark,
                    onSelected = { onThemeSelected(ThemeOption.Dark) }
                )
                ThemeRadioButton(
                    text = stringResource(R.string.theme_light),
                    isSelected = currentTheme == ThemeOption.Light,
                    onSelected = { onThemeSelected(ThemeOption.Light) }
                )
            }
        },
        confirmButton = {}
    )
}

@Composable
fun ThemeRadioButton(text: String, isSelected: Boolean, onSelected: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(enabled = true, onClick = onSelected)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = onSelected)
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
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