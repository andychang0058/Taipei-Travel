package com.cathaybk.travel.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttractionResponse(
    @SerialName("total") val total: Int,
    @SerialName("data") val data: List<Attraction>? = null,
)


@Serializable
data class Attraction(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String? = null,
    @SerialName("introduction") val introduction: String? = null,
    @SerialName("open_time") val openTime: String? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("tel") val tel: String? = null,
    @SerialName("fax") val fax: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("nlat") val nlat: Double? = null,
    @SerialName("elong") val elong: Double? = null,
    @SerialName("official_site") val officialSite: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("images") val images: List<Image>? = null
)