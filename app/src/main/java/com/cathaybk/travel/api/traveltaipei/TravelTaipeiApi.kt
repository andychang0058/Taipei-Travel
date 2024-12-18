package com.cathaybk.travel.api.traveltaipei

import com.cathaybk.travel.api.annotation.LanguageSupport
import com.cathaybk.travel.model.AttractionResponse
import com.cathaybk.travel.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TravelTaipeiApi {

    @LanguageSupport
    @GET("Attractions/All")
    suspend fun getAttractions(@Query("page") page: Int = 1): Result<AttractionResponse>

    @LanguageSupport
    @GET("Events/News")
    suspend fun getEventNews(@Query("page") page: Int = 1): Result<NewsResponse>
}