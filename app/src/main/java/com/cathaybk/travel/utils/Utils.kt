package com.cathaybk.travel.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

object Utils {

    fun openGoogleMapWithAddress(
        context: Context,
        lat: Double?,
        lng: Double?,
        address: String,
    ) {
        val encodedAddress = Uri.encode(address)
        val gmmIntentUri = Uri.parse("geo:${lat ?: ""},${lng ?: ""}?q=$encodedAddress")

        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query=$encodedAddress")
            )
            context.startActivity(browserIntent)
        }
    }
}