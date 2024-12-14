package com.cathaybk.travel.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.cathaybk.travel.ui.base.theme.TravelTheme

@Composable
fun DialogRadioOption(text: String, isSelected: Boolean, onSelected: () -> Unit) {
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
fun DialogRadioOptionPreview() {
    TravelTheme {
        Surface {
            DialogRadioOption(
                text = "Option 1",
                isSelected = true,
                onSelected = {}
            )
        }
    }
}
