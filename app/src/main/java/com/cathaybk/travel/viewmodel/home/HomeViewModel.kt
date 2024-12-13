package com.cathaybk.travel.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cathaybk.travel.manager.language.LanguageManager
import com.cathaybk.travel.repo.TravelRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
class HomeViewModel(
    private val travelRepo: TravelRepo,
    private val languageManager: LanguageManager,
) : ViewModel() {

    private var attractionSource: HomePagingSource? = null

    val pagingSource: Flow<PagingData<HomePagingData>> =
        Pager(
            config = PagingConfig(
                initialLoadSize = 30,
                pageSize = 30,
                prefetchDistance = 10,
                enablePlaceholders = false,
            )
        ) { HomePagingSource(travelRepo).also { attractionSource = it } }
            .flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            languageManager.displayLanguage.collect { refresh() }
        }
    }

    fun refresh() {
        attractionSource?.invalidate()
    }
}