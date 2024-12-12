package com.cathaybk.travel.model

sealed class RequestState {
    data object Loading : RequestState()
    data object Success : RequestState()
    data class Error(val message: String) : RequestState()
}