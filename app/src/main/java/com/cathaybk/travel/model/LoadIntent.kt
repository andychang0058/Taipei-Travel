package com.cathaybk.travel.model

sealed class LoadIntent {
    data object Refresh : LoadIntent()
    data object LoadMore : LoadIntent()
    data object Retry : LoadIntent()
    data object RetryLoadMore : LoadIntent()
}