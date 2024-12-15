package com.cathaybk.travel.extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun LazyListState.ObserveBottomReached(
    prefetchDistance: Int = 10,
    onReached: () -> Unit
) {
    val isReachedBottom by remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            lastVisibleItem.index != 0 &&
                    lastVisibleItem.index >= layoutInfo.totalItemsCount - prefetchDistance
        }
    }
    LaunchedEffect(isReachedBottom) {
        if (isReachedBottom) {
            onReached()
        }
    }
}