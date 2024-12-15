package com.cathaybk.travel.model

data class UiState<T>(
    val state: RequestState = RequestState.Refresh,
    val data: T? = null,
)