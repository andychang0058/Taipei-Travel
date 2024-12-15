package com.cathaybk.travel.viewmodel.home

import com.cathaybk.travel.model.Attraction
import com.cathaybk.travel.model.News

sealed class HomePagingData {
    abstract val id: String
    abstract val type: String

    data class NewsData(val news: List<News>) : HomePagingData() {
        override val id: String = news.joinToString { it.id }
        override val type: String = "News"
    }

    data class AttractionData(val attraction: Attraction) : HomePagingData() {
        override val id: String = attraction.id
        override val type: String = "Attraction"
    }
}