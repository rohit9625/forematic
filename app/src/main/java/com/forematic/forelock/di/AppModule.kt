package com.forematic.forelock.di

import android.telephony.SmsManager
import com.forematic.forelock.core.domain.InputValidator
import com.forematic.forelock.core.utils.MessageSender

interface AppModule {
    val inputValidator: InputValidator

    val smsManager: SmsManager

    val smsHelper: MessageSender
}