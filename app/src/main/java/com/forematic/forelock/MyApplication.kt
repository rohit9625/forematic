package com.forematic.forelock

import android.app.Application
import com.forematic.forelock.di.AppModule
import com.forematic.forelock.di.AppModuleImpl

class MyApplication: Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}