package com.cathaybk.travel.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.cathaybk.travel.R
import com.cathaybk.travel.model.Attraction
import com.cathaybk.travel.model.News
import com.cathaybk.travel.ui.common.LoadMoreError
import com.cathaybk.travel.ui.common.LoadMoreProgress
import com.cathaybk.travel.ui.common.LoadNoMoreData
import com.cathaybk.travel.ui.common.ScreenError
import com.cathaybk.travel.viewmodel.home.HomePagingData
import com.cathaybk.travel.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    onNewsClicked: (News) -> Unit = {},
    onMoreNewsClicked: () -> Unit = {},
    onAttractionClicked: (Attraction) -> Unit = {},
) {
    val viewModel: HomeViewModel = koinViewModel()
    val lazyListState = rememberLazyListState()
    val pagingItems = viewModel.pagingSource.collectAsLazyPagingItems()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = pagingItems.loadState.refresh is LoadState.Loading,
        onRefresh = { viewModel.refresh() }
    )

    if (pagingItems.loadState.refresh is LoadState.Error) {
        ScreenError(onRetry = { viewModel.refresh() })
        return
    }

    val firstAttractionId by remember(pagingItems) {
        derivedStateOf {
            (0 until pagingItems.itemCount)
                .firstOrNull { pagingItems[it] is HomePagingData.AttractionData }
                ?.let { pagingItems[it]?.id }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            items(
                count = pagingItems.itemCount,
                key = { index -> pagingItems[index]?.id ?: index }
            ) { index ->
                val item = pagingItems[index] ?: return@items
                when (item) {
                    is HomePagingData.NewsData -> {
                        if (item.news.isEmpty()) return@items

                        HomeSectionTitle(
                            modifier = Modifier.padding(8.dp),
                            title = stringResource(R.string.news),
                        )
                        HomeNewsSection(
                            news = item.news,
                            onNewsClicked = onNewsClicked,
                            onMoreClicked = onMoreNewsClicked,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }

                    is HomePagingData.AttractionData -> {
                        val attraction = item.attraction
                        val description by remember(attraction) {
                            mutableStateOf(attraction.introduction?.replace(Regex("\\n"), ""))
                        }
                        if (attraction.id == firstAttractionId) {
                            HomeSectionTitle(
                                modifier = Modifier.padding(8.dp),
                                title = stringResource(R.string.attractions),
                            )
                        }
                        HomeAttractionCell(
                            modifier = Modifier.padding(bottom = 24.dp),
                            imageUrl = attraction.images?.firstOrNull()?.src.orEmpty(),
                            title = attraction.name.orEmpty(),
                            description = description.orEmpty(),
                            onCellClicked = { onAttractionClicked(attraction) },
                        )
                    }
                }
            }

            when (val appendState = pagingItems.loadState.append) {
                is LoadState.Loading -> {
                    item { LoadMoreProgress() }
                }

                is LoadState.Error -> {
                    item { LoadMoreError(onRetry = { pagingItems.retry() }) }
                }

                is LoadState.NotLoading -> {
                    if (appendState.endOfPaginationReached.not()) return@LazyColumn
                    item { LoadNoMoreData() }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = pagingItems.loadState.refresh is LoadState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
