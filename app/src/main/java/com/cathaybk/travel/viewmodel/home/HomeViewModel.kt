package com.cathaybk.travel.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cathaybk.travel.manager.language.LanguageManager
import com.cathaybk.travel.model.LoadIntent
import com.cathaybk.travel.model.RequestState
import com.cathaybk.travel.model.UiState
import com.cathaybk.travel.repo.TravelRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
class HomeViewModel(
    private val travelRepo: TravelRepo,
    private val languageManager: LanguageManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState<List<HomePagingData>>())
    val uiState: StateFlow<UiState<List<HomePagingData>>> = _uiState

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
            val newsDeferred = async { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) }
            val attractionsDeferred = async { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) }

            val (news, attractions) = newsDeferred.await() to attractionsDeferred.await()

            if (news.isFailure && attractions.isFailure) {
                val error = news.exceptionOrNull()
                    ?: attractions.exceptionOrNull()
                    ?: Exception("Unknown error")

                _uiState.update {
                    UiState(state = RequestState.RefreshError(error))
                }
                return@launch
            }

            val attractionData = attractions.getOrNull()?.data ?: emptyList()
            val newsData = news.getOrNull()?.data ?: emptyList()
            isReachedEnd = attractionData.size < TravelRepo.PAGE_SIZE

            val result = buildList<HomePagingData> {
                if (newsData.isNotEmpty()) {
                    add(HomePagingData.NewsData(news = newsData))
                }
                attractionData.forEach {
                    add(HomePagingData.AttractionData(it))
                }
            }

            _uiState.update {
                it.copy(
                    data = result,
                    state = RequestState.Success(isReachedEnd = isReachedEnd)
                )
            }
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
            travelRepo.getAttractions(page)
                .onFailure { error ->
                    _uiState.update {
                        it.copy(state = RequestState.LoadError(error))
                    }
                }
                .onSuccess {
                    val result = it.data?.map { HomePagingData.AttractionData(it) } ?: emptyList()
                    isReachedEnd = (it.data?.size ?: 0) < TravelRepo.PAGE_SIZE

                    _uiState.update {
                        it.copy(
                            data = it.data?.plus(result),
                            state = RequestState.Success(isReachedEnd = isReachedEnd)
                        )
                    }
                }
        }
    }
}