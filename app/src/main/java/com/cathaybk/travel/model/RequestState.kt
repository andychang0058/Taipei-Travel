package com.cathaybk.travel.model

sealed class RequestState {
    data object Refresh : RequestState()
    data object Loading : RequestState()
    data class Success(val isReachedEnd: Boolean = false) : RequestState()
    data class RefreshError(val error: Throwable) : RequestState()
    data class LoadError(val error: Throwable) : RequestState()
}