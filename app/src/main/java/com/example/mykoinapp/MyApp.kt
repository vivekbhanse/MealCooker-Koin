package com.example.mykoinapp

import android.app.Application
import com.example.mykoinapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            loadKoinModules(appModule)
        }

    }
}