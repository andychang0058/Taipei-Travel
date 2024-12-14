package com.cathaybk.travel.ui.attraction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cathaybk.travel.R
import com.cathaybk.travel.ui.base.theme.TravelTheme


@Composable
fun AttractionIconCell(
    modifier: Modifier = Modifier,
    iconResId: Int,
    text: String,
    maxLines: Int = Int.MAX_VALUE,
    clickable: Boolean = false,
    onClicked: () -> Unit = {},
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = clickable, onClick = onClicked)
            .padding(vertical = 8.dp, horizontal = 4.dp),
    ) {
        val (icon, label, arrow) = createRefs()
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "",
            modifier = Modifier
                .size(24.dp)
                .constrainAs(icon) {
                    start.linkTo(parent.start)
                    top.linkTo(label.top)
                }
        )

        Text(
            modifier = Modifier
                .constrainAs(label) {
                    if (clickable) {
                        linkTo(start = icon.end, end = arrow.start)
                    } else {
                        linkTo(start = icon.end, end = parent.end)
                    }
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                }
                .padding(horizontal = 8.dp),
            text = text,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )

        if (clickable) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(arrow) {
                        end.linkTo(parent.end)
                        linkTo(top = label.top, bottom = label.bottom)
                    }
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AttractionIconCellPreview() {
    TravelTheme {
        Surface {
            Column {
                AttractionIconCell(
                    iconResId = R.drawable.ic_location,
                    text = "Taipei 101",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    clickable = true
                )
                AttractionIconCell(
                    iconResId = R.drawable.ic_schedule,
                    text = "週二至週日 11:00 - 21:00　\r\n週一 休館日\r\n*遇國定假日或連續假日延後一日休館",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    clickable = true
                )
                AttractionIconCell(
                    iconResId = R.drawable.ic_schedule,
                    text = "週二至週日 11:00 - 21:00 週一 休館日 *遇國定假日或連續假日延後一日休館",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    clickable = false
                )
                AttractionIconCell(
                    iconResId = R.drawable.ic_schedule,
                    text = "週二至週日 11:00 - 21:00　\n" +
                            "週一 休館日\n" +
                            "*遇國定假日或連續假日延後一日休館",
                    maxLines = 2,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    clickable = false
                )
            }
        }
    }
}
