package com.cathaybk.travel.repo

import com.cathaybk.travel.model.AttractionResponse
import com.cathaybk.travel.model.NewsResponse

interface TravelRepo {
    companion object {
        const val INITIAL_PAGE = 1
        const val PAGE_SIZE = 30
    }

    suspend fun getAttractions(page: Int): Result<AttractionResponse>
    suspend fun getEventNews(page: Int): Result<NewsResponse>
}