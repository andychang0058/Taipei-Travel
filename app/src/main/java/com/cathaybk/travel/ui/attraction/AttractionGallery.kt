package com.cathaybk.travel.ui.attraction

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.cathaybk.travel.ui.base.theme.TravelTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AttractionGallery(
    modifier: Modifier = Modifier,
    images: List<String>,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        val pagerState = rememberPagerState(pageCount = { images.size })

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            AsyncImage(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .aspectRatio(16f / 9f),
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(images[page])
                    .build(),
                contentDescription = "Cover Image",
                contentScale = ContentScale.Crop,
            )
        }

        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(images.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) {
                        Color.White
                    } else {
                        Color.White.copy(alpha = 0.3f)
                    }
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(4.dp)
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun AttractionGalleryPreview() {
    TravelTheme {
        Surface {
            AttractionGallery(
                images = listOf(
                    "https://images.unsplash.com/photo-1629781220000-4b3b3b3b3b3b",
                    "https://images.unsplash.com/photo-1629781220000-4b3b3b3b3b3b",
                    "https://images.unsplash.com/photo-1629781220000-4b3b3b3b3b3b",
                    "https://images.unsplash.com/photo-1629781220000-4b3b3b3b3b3b",
                    "https://images.unsplash.com/photo-1629781220000-4b3b3b3b3b3b",
                )
            )
        }
    }
}