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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cathaybk.travel.R
import com.cathaybk.travel.extensions.ObserveBottomReached
import com.cathaybk.travel.model.Attraction
import com.cathaybk.travel.model.LoadIntent
import com.cathaybk.travel.model.News
import com.cathaybk.travel.model.RequestState
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    lazyListState.ObserveBottomReached {
        viewModel.loadIntent(LoadIntent.LoadMore)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.state is RequestState.Refresh,
        onRefresh = {
            viewModel.loadIntent(LoadIntent.Refresh)
        }
    )

    val firstAttractionId by remember {
        derivedStateOf {
            uiState.data?.firstOrNull { it is HomePagingData.AttractionData }?.id
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
                count = uiState.data?.size ?: 0,
                contentType = { index -> uiState.data?.getOrNull(index)?.javaClass },
                key = { index -> uiState.data?.getOrNull(index)?.id.orEmpty() }
            ) { index ->
                when (val item = uiState.data?.getOrNull(index) ?: return@items) {
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

                        if (firstAttractionId.isNullOrEmpty().not()) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }

                    is HomePagingData.AttractionData -> {
                        val attraction = item.attraction
                        val description = remember(attraction.introduction) {
                            attraction.introduction?.replace(Regex("\\n"), "").orEmpty()
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
                            description = description,
                            onCellClicked = { onAttractionClicked(attraction) },
                        )
                    }
                }
            }

            when (val state = uiState.state) {
                is RequestState.Loading -> {
                    item { LoadMoreProgress() }
                }

                is RequestState.LoadError -> {
                    item {
                        LoadMoreError(
                            onRetry = { viewModel.loadIntent(LoadIntent.RetryLoadMore) }
                        )
                    }
                }

                is RequestState.Success -> {
                    if (uiState.data?.isEmpty() == true) {
                        item {
                            ScreenError(
                                modifier = Modifier.fillParentMaxSize(),
                                message = stringResource(R.string.empty_data),
                                onRetry = { viewModel.loadIntent(LoadIntent.Refresh) }
                            )
                        }
                    }

                    if (state.isReachedEnd && firstAttractionId.isNullOrEmpty().not()) {
                        item { LoadNoMoreData() }
                    }
                }

                is RequestState.RefreshError -> {
                    item {
                        ScreenError(
                            modifier = Modifier.fillParentMaxSize(),
                            message = stringResource(R.string.error_occurred),
                            onRetry = { viewModel.loadIntent(LoadIntent.Refresh) }
                        )
                    }
                }

                else -> {}
            }
        }

        PullRefreshIndicator(
            refreshing = uiState.state is RequestState.Refresh,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
