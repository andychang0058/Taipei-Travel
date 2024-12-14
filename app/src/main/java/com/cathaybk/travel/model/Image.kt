package com.cathaybk.travel.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class Image(
    @SerialName("src") val src: String? = null,
    @SerialName("ext") val ext: String? = null,
) : Parcelable