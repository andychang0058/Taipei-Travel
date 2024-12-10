package com.cathaybk.travel.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NewsResponse(
    @SerialName("total") val total: Int,
    @SerialName("data") val data: List<News>? = null,
)

@Serializable
data class News(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("posted") val posted: String? = null,
)
