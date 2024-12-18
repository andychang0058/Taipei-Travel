package com.cathaybk.travel.api.traveltaipei

import com.cathaybk.travel.api.ApiDomainContract
import com.cathaybk.travel.api.base.ApiCallAdapterFactory
import com.cathaybk.travel.api.base.KotlinSerializationConverterFactory
import com.cathaybk.travel.api.interceptor.LanguageInterceptor
import com.cathaybk.travel.api.interceptor.TravelTaipeiInterceptor
import com.cathaybk.travel.api.json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class TravelApiProvider(
    private val travelTaipeiInterceptor: TravelTaipeiInterceptor,
    private val languageInterceptor: LanguageInterceptor,
) {
    val api: TravelTaipeiApi by lazy {
        youTubeRetrofit.create(TravelTaipeiApi::class.java)
    }

    private val retrofitBuilder: Retrofit.Builder
        get() = Retrofit.Builder()
            .addCallAdapterFactory(ApiCallAdapterFactory())
            .addConverterFactory(KotlinSerializationConverterFactory(json))

    private val okhttpBuilder: OkHttpClient.Builder
        get() = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BASIC)
                }
            )
            .addInterceptor(travelTaipeiInterceptor)
            .addInterceptor(languageInterceptor)

    private val youTubeRetrofit: Retrofit by lazy {
        retrofitBuilder
            .baseUrl(ApiDomainContract.TRAVEL_TAIPEI)
            .client(okhttpBuilder.build())
            .build()
    }
}