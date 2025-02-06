package com.forematic.forelock.core.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import com.forematic.forelock.core.domain.model.MessageListener

class MessageReceiver(
    private val messageListener: MessageListener
): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle: Bundle? = intent?.extras

        bundle?.let {
            val pdus = it.get("pdus") as Array<*>

            for(pdu in pdus) {
                val smsFormat = it.getString("format")
                val sms = SmsMessage.createFromPdu(pdu as ByteArray, smsFormat)
                val senderAddress = sms.originatingAddress
                val messageBody = sms.messageBody

                senderAddress?.let {
                    messageListener.onMessageReceived(senderAddress, messageBody)
                }
            }
        }
    }

    companion object {
        const val SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"
    }
}