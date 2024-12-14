package com.cathaybk.travel.ui.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.cathaybk.travel.ui.base.theme.TravelTheme

@Composable
fun NewsCell(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    date: String,
    onClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = true, onClick = onClicked)
            .padding(8.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = description,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = date,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}


@PreviewLightDark
@Composable
fun NewsCellPreview() {
    TravelTheme {
        Surface {
            NewsCell(
                title = "Golden Gate Bridge",
                description = "This is the new title for a Jetpack Compose preview component. It should be descriptive, concise, and follow the Material Design guidelines. The title's maximum line limit is 2. Could you suggest ways to rephrase or extend this while keeping it meaningful and ensuring it fits within the two-line constraint?",
                date = "2021-09-01",
                onClicked = {}
            )
        }
    }
}