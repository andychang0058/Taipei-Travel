package com.cathaybk.travel.ui

import com.cathaybk.travel.model.Attraction
import kotlinx.serialization.Serializable

sealed class Screen() {
    @Serializable
    data object Home

    @Serializable
    data object News

    @Serializable
    data class AttractionDetail(val attraction: Attraction)
}