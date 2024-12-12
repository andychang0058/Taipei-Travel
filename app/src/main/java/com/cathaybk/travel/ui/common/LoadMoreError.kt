package com.cathaybk.travel.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.cathaybk.travel.R
import com.cathaybk.travel.ui.theme.TravelTheme

@Composable
fun LoadMoreError(onRetry: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}

@PreviewLightDark
@Composable
fun LoadMoreErrorPreview() {
    TravelTheme {
        Surface { LoadMoreError() }
    }
}