package com.echo.app

import android.app.Application
import com.echo.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class EchoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            printLogger(Level.DEBUG)
            androidContext(this@EchoApplication)
            modules(appModule)
        }
    }
}