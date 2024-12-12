package com.cathaybk.travel.viewmodel.home

import com.cathaybk.travel.model.Attraction
import com.cathaybk.travel.model.News

sealed class HomePagingData {
    abstract val id: String

    data class NewsData(val news: List<News>) : HomePagingData() {
        override val id: String = news.joinToString { it.id }
    }
    data class AttractionData(val attraction: Attraction) : HomePagingData() {
        override val id: String = attraction.id
    }
}