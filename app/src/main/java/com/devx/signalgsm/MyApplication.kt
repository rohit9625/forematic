package com.devx.signalgsm

import android.app.Application
import com.devx.signalgsm.di.AppModule
import com.devx.signalgsm.di.AppModuleImpl

class MyApplication: Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}