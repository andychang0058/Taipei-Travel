package com.cathaybk.travel.api.traveltaipei

import com.cathaybk.travel.api.base.BaseResultCallAdapter
import java.lang.reflect.Type

class TravelTaipeiCallAdapter<T>(responseType: Type) :
    BaseResultCallAdapter<T>(responseType) {

    override fun convertErrorBody(
        code: Int,
        errorString: String,
        message: String?,
    ): Exception? {
        /**
         * If we have a custom error body, we can parse it here.
         */
        return null
    }
}