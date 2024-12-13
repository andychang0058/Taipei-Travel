package com.cathaybk.travel.viewmodel.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cathaybk.travel.manager.language.LanguageManager
import com.cathaybk.travel.model.News
import com.cathaybk.travel.repo.TravelRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
class NewsViewModel(
    private val travelRepo: TravelRepo,
    private val languageManager: LanguageManager,
) : ViewModel() {

    private var newsPagingSource: NewsPagingSource? = null

    val pagingSource: Flow<PagingData<News>> =
        Pager(
            config = PagingConfig(
                initialLoadSize = 30,
                pageSize = 30,
                prefetchDistance = 10,
                maxSize = 120,
                enablePlaceholders = false,
            )
        ) { NewsPagingSource(travelRepo).also { newsPagingSource = it } }
            .flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            languageManager.displayLanguage.collect { refresh() }
        }
    }

    fun refresh() {
        newsPagingSource?.invalidate()
    }
}