package com.cathaybk.travel.ui.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Surface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.cathaybk.travel.model.News
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
                when (pagingItems.loadState.prepend) {
                    is LoadState.Loading -> {
                        item { LoadMoreProgress() }
                    }

                    else -> {}
                }

                items(
                    count = pagingItems.itemCount,
                    key = { index -> pagingItems[index]?.id ?: index }
                ) { index ->
                    val item = pagingItems[index] ?: return@items

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

}