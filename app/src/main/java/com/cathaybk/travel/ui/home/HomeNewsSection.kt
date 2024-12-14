package com.cathaybk.travel.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.cathaybk.travel.R
import com.cathaybk.travel.model.News
import com.cathaybk.travel.ui.base.theme.TravelTheme

@Composable
fun HomeNewsSection(
    news: List<News>,
    onNewsClicked: (News) -> Unit = {},
    onMoreClicked: () -> Unit = {},
) {
    Column {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            news.take(3).forEach { news ->
                HomeNewsCell(
                    title = news.title.orEmpty(),
                    date = news.posted.orEmpty(),
                    onCellClicked = { onNewsClicked(news) }
                )
            }
        }

        if (news.size > 3) {
            HomeSeeMoreNews(modifier = Modifier, onClick = onMoreClicked)
        } else {
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@Composable
fun HomeNewsCell(
    title: String,
    date: String,
    onCellClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = true, onClick = onCellClicked)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = date,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun HomeSeeMoreNews(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = true, onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.more),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = "More"
        )
    }
}


@Composable
@PreviewLightDark
fun HomeNewsSectionPreview() {
    val title =
        "This is the new title for a Jetpack Compose preview component. It should be descriptive, concise, and follow the Material Design guidelines. The title's maximum line limit is 2. Could you suggest ways to rephrase or extend this while keeping it meaningful and ensuring it fits within the two-line constraint?"
    TravelTheme {
        Surface {
            HomeNewsSection(
                news = listOf(
                    News(id = "id", title = title, posted = "2021-09-01"),
                    News(id = "id", title = title, posted = "2021-09-01"),
                    News(id = "id", title = title, posted = "2021-09-01"),
                    News(id = "id", title = title, posted = "2021-09-01"),
                )
            )
        }
    }
}