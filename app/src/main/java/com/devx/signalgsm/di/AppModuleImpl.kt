package com.devx.signalgsm.di

import android.content.Context
import android.os.Build
import android.telephony.SmsManager

class AppModuleImpl(private val context: Context): AppModule {
    override val smsManager: SmsManager by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(SmsManager::class.java)
        } else {
            SmsManager.getDefault()
        }
    }
}