package com.cathaybk.travel.ui.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cathaybk.travel.R
import com.cathaybk.travel.extensions.ObserveBottomReached
import com.cathaybk.travel.model.LoadIntent
import com.cathaybk.travel.model.News
import com.cathaybk.travel.model.RequestState
import com.cathaybk.travel.ui.common.LoadMoreError
import com.cathaybk.travel.ui.common.LoadMoreProgress
import com.cathaybk.travel.ui.common.LoadNoMoreData
import com.cathaybk.travel.ui.common.ScreenError
import com.cathaybk.travel.viewmodel.news.NewsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsScreen(
    onNewsClicked: (News) -> Unit = {},
) {
    val viewModel: NewsViewModel = koinViewModel()
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

    Surface {
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
                    key = { index -> uiState.data?.getOrNull(index)?.id ?: index }
                ) { index ->
                    val item = uiState.data?.getOrNull(index) ?: return@items

                    val description by remember(item) {
                        mutableStateOf(
                            item.description?.replace(Regex("\\R|\\n|&nbsp;|&#160;|\\s+"), "")
                        )
                    }

                    NewsCell(
                        modifier = Modifier.padding(bottom = 16.dp),
                        title = item.title.orEmpty(),
                        description = description.orEmpty(),
                        date = item.posted.orEmpty(),
                        onClicked = { onNewsClicked(item) }
                    )
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

                        if (state.isReachedEnd && uiState.data?.isNotEmpty() == true) {
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

}