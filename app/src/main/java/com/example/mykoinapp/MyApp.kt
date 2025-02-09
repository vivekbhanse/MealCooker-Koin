package com.example.mykoinapp

import android.app.Application
import com.example.mykoinapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import timber.log.Timber.Forest.plant


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }

        startKoin {
            logger(AndroidLogger(Level.DEBUG))
            androidContext(this@MyApp)
            modules(appModule)
        }

    }
}