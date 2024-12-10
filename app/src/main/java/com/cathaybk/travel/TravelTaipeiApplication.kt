package com.cathaybk.travel

import android.app.Application
import com.cathaybk.travel.di.TravelTaipeiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class TravelTaipeiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TravelTaipeiApplication)
            modules(TravelTaipeiModule().module)
        }
    }
}