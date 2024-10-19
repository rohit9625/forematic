package com.devx.signalgsm.di

import android.telephony.SmsManager

interface AppModule {
    val smsManager: SmsManager
}