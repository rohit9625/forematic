package com.forematic.forelock.core.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage

class MessageReceiver(
    private val onMessageReceived: ((senderAddress: String?, receivedMessage: String?) -> Unit)? = null
): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == SMS_RECEIVED_ACTION) {
            val bundle: Bundle? = intent.extras

            bundle?.let {
                val pdus = it.get("pdus") as Array<*>

                for(pdu in pdus) {
                    val smsFormat = it.getString("format")
                    val sms = SmsMessage.createFromPdu(pdu as ByteArray, smsFormat)
                    val senderAddress = sms.originatingAddress
                    val messageBody = sms.messageBody

                    onMessageReceived?.invoke(senderAddress, messageBody)
                }
            }
        }
    }

    companion object {
        const val SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"
    }
}