package com.cathaybk.travel.model

data class UiState<T>(
    val state: RequestState = RequestState.Loading,
    val data: T? = null,
)