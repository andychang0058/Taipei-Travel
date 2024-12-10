package com.cathaybk.travel.di

import com.cathaybk.travel.api.traveltaipei.TravelApiProvider
import com.cathaybk.travel.api.traveltaipei.TravelTaipeiApi
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.cathaybk.travel")
class TravelTaipeiModule {
    @Single
    internal fun providerTravelApi(): TravelTaipeiApi {
        return TravelApiProvider().api
    }
}