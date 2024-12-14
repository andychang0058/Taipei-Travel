package com.cathaybk.travel.ui.attraction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cathaybk.travel.R
import com.cathaybk.travel.model.Attraction

@Composable
fun AttractionScreen(
    attraction: Attraction,
    onOfficialSiteClicked: (String) -> Unit = {},
    onLocationClicked: (Double?, Double?, String) -> Unit = { _, _, _ -> }
) {
    val scrollState = rememberScrollState()
    Surface(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            AttractionGallery(images = attraction.images?.map { it.src.orEmpty() } ?: emptyList())

            Text(
                text = attraction.name.orEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(top = 24.dp, bottom = 24.dp),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            var cellCount = 0

            if (attraction.openTime.isNullOrEmpty().not()) {
                cellCount++
                AttractionIconCell(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    iconResId = R.drawable.ic_schedule,
                    text = attraction.openTime,
                )
            }

            if (attraction.address.isNullOrEmpty().not()) {
                cellCount++
                if (cellCount > 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        )
                    )
                }
                AttractionIconCell(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    iconResId = R.drawable.ic_location,
                    text = attraction.address,
                    clickable = true,
                    onClicked = {
                        onLocationClicked(null, null, attraction.address)
                    }
                )
            }

            if (attraction.officialSite.isNullOrEmpty().not()) {
                cellCount++
                if (cellCount > 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        )
                    )
                }
                AttractionIconCell(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    iconResId = R.drawable.ic_public,
                    text = attraction.officialSite,
                    maxLines = 2,
                    clickable = true,
                    onClicked = { onOfficialSiteClicked(attraction.officialSite) }
                )
            }

            if (attraction.tel.isNullOrEmpty().not()) {
                cellCount++
                if (cellCount > 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        )
                    )
                }
                AttractionIconCell(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    iconResId = R.drawable.ic_call,
                    text = attraction.tel,
                    clickable = false,
                )
            }

            if (attraction.email.isNullOrEmpty().not()) {
                cellCount++
                if (cellCount > 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        )
                    )
                }
                AttractionIconCell(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    iconResId = R.drawable.ic_mail,
                    text = attraction.email,
                    clickable = false,
                )
            }

            if (attraction.introduction.isNullOrEmpty().not()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 12.dp),
                    text = attraction.introduction,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}