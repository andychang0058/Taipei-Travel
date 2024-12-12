package com.cathaybk.travel.repo

import com.cathaybk.travel.api.traveltaipei.TravelTaipeiApi
import com.cathaybk.travel.model.AttractionResponse
import com.cathaybk.travel.model.NewsResponse
import org.koin.core.annotation.Factory
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Factory
class TravelRepoImpl(
    private val travelApi: TravelTaipeiApi,
) : TravelRepo {

    override suspend fun getAttractions(page: Int): Result<AttractionResponse> {
        return travelApi.getAttractions(page)
    }

    override suspend fun getEventNews(page: Int): Result<NewsResponse> {
        return travelApi.getEventNews(page).map { response ->
            response.copy(
                data = response.data?.map { news ->
                    try {
                        news.copy(posted = formatDateTime(news.posted.orEmpty()))
                    } catch (e: Exception) {
                        news
                    }
                }
            )
        }
    }

    private fun formatDateTime(dateTime: String): String {
        if (dateTime.isEmpty()) {
            return dateTime
        }
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss xxx")
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            OffsetDateTime.parse(dateTime, formatter).toLocalDateTime().format(outputFormatter)
        } catch (e: Exception) {
            dateTime
        }
    }

}