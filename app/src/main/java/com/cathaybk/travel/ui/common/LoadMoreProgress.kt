package com.cathaybk.travel.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.cathaybk.travel.ui.base.theme.TravelTheme

@Composable
fun LoadMoreProgress() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@PreviewLightDark
@Composable
fun LoadMoreProgressPreview() {
    TravelTheme {
        Surface { LoadMoreProgress() }
    }
}