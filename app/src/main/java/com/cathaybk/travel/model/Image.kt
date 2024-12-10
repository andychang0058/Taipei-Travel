package com.cathaybk.travel.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("src") val src: String?,
    @SerialName("ext") val ext: String?,
)