package com.cathaybk.travel.repo

import com.cathaybk.travel.api.traveltaipei.TravelTaipeiApi
import com.cathaybk.travel.model.AttractionResponse
import com.cathaybk.travel.model.NewsResponse
import org.koin.core.annotation.Factory

@Factory
class TravelRepoImpl(
    private val travelApi: TravelTaipeiApi,
) : TravelRepo {

    override suspend fun getAttractions(page: Int): Result<AttractionResponse> {
        return travelApi.getAttractions(page)
    }

    override suspend fun getEventNews(page: Int): Result<NewsResponse> {
        return travelApi.getEventNews(page)
    }
}