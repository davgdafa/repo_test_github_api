package com.fifa.test

import android.app.Application
import com.fifa.data.di.dataModule
import com.fifa.domain.di.domainModule
import com.fifa.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class FifaPlayersApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@FifaPlayersApplication)
            modules(presentationModule, dataModule, domainModule)
        }
    }
}
