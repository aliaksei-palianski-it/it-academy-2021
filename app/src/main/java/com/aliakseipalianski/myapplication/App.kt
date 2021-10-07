package com.aliakseipalianski.myapplication

import android.app.Application
import com.aliakseipalianski.core.koin.createCoreModule
import com.aliakseipalianski.myapplication.common.createAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(createAppModule(), createCoreModule())
        }
    }
}