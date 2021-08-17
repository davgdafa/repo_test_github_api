package com.got.test

import android.app.Application
import com.got.data.di.dataModule
import com.got.domain.di.domainModule
import com.got.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class GotPlayersApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@GotPlayersApplication)
            modules(presentationModule, dataModule, domainModule)
        }
    }
}
