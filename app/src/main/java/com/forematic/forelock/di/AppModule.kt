package com.forematic.forelock.di

import android.telephony.SmsManager
import com.forematic.forelock.core.domain.InputValidator

interface AppModule {
    val inputValidator: InputValidator

    val smsManager: SmsManager
}