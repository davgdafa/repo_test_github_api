package com.got.test

import android.app.Application
import com.got.facade.di.dataModule
import com.got.facade.di.domainModule
import com.got.facade.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class GotCharactersApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@GotCharactersApplication)
            modules(presentationModule, dataModule, domainModule)
        }
    }
}
