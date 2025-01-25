package com.forematic.forelock.di

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import com.forematic.forelock.core.domain.InputValidator
import com.forematic.forelock.core.utils.MessageSender

class AppModuleImpl(private val context: Context): AppModule {
    override val inputValidator: InputValidator by lazy {
        InputValidator()
    }

    override val smsManager: SmsManager by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(SmsManager::class.java)
        } else {
            SmsManager.getDefault()
        }
    }
    override val smsHelper: MessageSender by lazy {
        MessageSender(context, smsManager)
    }
}