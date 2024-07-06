package com.saal.androidtest

import android.app.Application
import com.saal.androidtest.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AndroidTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AndroidTestApplication)
            modules(appModule)
        }
    }
}