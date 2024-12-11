package com.cathaybk.travel.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.cathaybk.travel.R
import com.cathaybk.travel.extensions.wrapWithLocale
import com.cathaybk.travel.manager.language.Language

@Composable
fun LanguageChoiceDialog(
    selectedLanguage: Language?,
    onLanguageSelected: (Language?) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.select_language))
        },
        text = {
            Column {
                DialogRadioOption(
                    text = stringResource(R.string.language_system),
                    isSelected = selectedLanguage == null,
                    onSelected = { onLanguageSelected(null) }
                )

                RadioOptionWithLanguage(
                    tag = Language.English.tag,
                    isSelected = selectedLanguage == Language.English,
                    onSelected = { onLanguageSelected(Language.English) }
                )

                RadioOptionWithLanguage(
                    tag = Language.ChineseTraditional.tag,
                    isSelected = selectedLanguage == Language.ChineseTraditional,
                    onSelected = { onLanguageSelected(Language.ChineseTraditional) }
                )

                RadioOptionWithLanguage(
                    tag = Language.ChineseSimplified.tag,
                    isSelected = selectedLanguage == Language.ChineseSimplified,
                    onSelected = { onLanguageSelected(Language.ChineseSimplified) }
                )

                RadioOptionWithLanguage(
                    tag = Language.Japanese.tag,
                    isSelected = selectedLanguage == Language.Japanese,
                    onSelected = { onLanguageSelected(Language.Japanese) }
                )

                RadioOptionWithLanguage(
                    tag = Language.Korean.tag,
                    isSelected = selectedLanguage == Language.Korean,
                    onSelected = { onLanguageSelected(Language.Korean) }
                )

                RadioOptionWithLanguage(
                    tag = Language.Spanish.tag,
                    isSelected = selectedLanguage == Language.Spanish,
                    onSelected = { onLanguageSelected(Language.Spanish) }
                )

                RadioOptionWithLanguage(
                    tag = Language.Indonesian.tag,
                    isSelected = selectedLanguage == Language.Indonesian,
                    onSelected = { onLanguageSelected(Language.Indonesian) }
                )

                RadioOptionWithLanguage(
                    tag = Language.Thai.tag,
                    isSelected = selectedLanguage == Language.Thai,
                    onSelected = { onLanguageSelected(Language.Thai) }
                )

                RadioOptionWithLanguage(
                    tag = Language.Vietnamese.tag,
                    isSelected = selectedLanguage == Language.Vietnamese,
                    onSelected = { onLanguageSelected(Language.Vietnamese) }
                )
            }
        },
        confirmButton = {}
    )
}

@Composable
fun RadioOptionWithLanguage(tag: String, isSelected: Boolean, onSelected: () -> Unit) {
    val context = LocalContext.current
    val languageContext by remember(tag) {
        mutableStateOf(context.wrapWithLocale(tag))
    }
    CompositionLocalProvider(LocalContext provides languageContext) {
        DialogRadioOption(
            text = stringResource(R.string.language),
            isSelected = isSelected,
            onSelected = onSelected
        )
    }
}