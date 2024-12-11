package com.cathaybk.travel.repo

import com.cathaybk.travel.model.AttractionResponse
import com.cathaybk.travel.model.NewsResponse

interface TravelRepo {
    suspend fun getAttractions(page: Int): Result<AttractionResponse>
    suspend fun getEventNews(page: Int): Result<NewsResponse>
}