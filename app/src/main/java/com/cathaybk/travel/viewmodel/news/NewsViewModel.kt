package com.cathaybk.travel.viewmodel.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cathaybk.travel.manager.language.LanguageManager
import com.cathaybk.travel.model.LoadIntent
import com.cathaybk.travel.model.News
import com.cathaybk.travel.model.RequestState
import com.cathaybk.travel.model.UiState
import com.cathaybk.travel.repo.TravelRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import kotlin.collections.orEmpty

@Factory
class NewsViewModel(
    private val travelRepo: TravelRepo,
    private val languageManager: LanguageManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState<List<News>>())
    val uiState: StateFlow<UiState<List<News>>> = _uiState

    private var page = TravelRepo.INITIAL_PAGE
    private var isReachedEnd = false

    init {
        refresh()
        viewModelScope.launch {
            languageManager.displayLanguage.drop(1).collect { refresh() }
        }
    }

    fun loadIntent(intent: LoadIntent) {
        when (intent) {
            is LoadIntent.Refresh -> refresh()
            is LoadIntent.LoadMore -> loadMore()
            is LoadIntent.Retry -> retry()
            is LoadIntent.RetryLoadMore -> loadMore(isRetry = true)
        }
    }

    private fun refresh() {
        page = TravelRepo.INITIAL_PAGE
        isReachedEnd = false

        _uiState.update {
            it.copy(state = RequestState.Refresh)
        }

        viewModelScope.launch(Dispatchers.IO) {
            travelRepo.getEventNews(TravelRepo.INITIAL_PAGE)
                .onFailure { error -> onFailure(error, false) }
                .onSuccess { onSuccess(it.data) }
        }
    }

    private fun retry() {
        _uiState.update {
            it.copy(data = null)
        }
        refresh()
    }

    private fun loadMore(isRetry: Boolean = false) {
        if (isReachedEnd) {
            return
        }

        if (_uiState.value.state is RequestState.Loading) {
            return
        }

        _uiState.update {
            it.copy(state = RequestState.Loading)
        }
        page = if (isRetry) page else page + 1

        viewModelScope.launch(Dispatchers.IO) {
            travelRepo.getEventNews(page)
                .onFailure { error -> onFailure(error, true) }
                .onSuccess { onSuccess(it.data) }
        }
    }

    private fun onFailure(error: Throwable, isLoadMoreError: Boolean) {
        _uiState.update {
            if (isLoadMoreError) {
                it.copy(state = RequestState.LoadError(error))
            } else {
                it.copy(state = RequestState.RefreshError(error))
            }
        }
    }

    private fun onSuccess(news: List<News>?) {
        isReachedEnd = (news?.size ?: 0) < TravelRepo.PAGE_SIZE
        _uiState.update {
            it.copy(
                data = it.data.orEmpty() + (news.orEmpty()),
                state = RequestState.Success(isReachedEnd = isReachedEnd)
            )
        }
    }
}