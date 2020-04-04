package com.systemathic.flagsquizz.base

import android.app.Application
import com.systemathic.flagsquizz.di.applicationModule
import org.koin.android.ext.android.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        startKoin(this, listOf(applicationModule))
        super.onCreate()
    }
}