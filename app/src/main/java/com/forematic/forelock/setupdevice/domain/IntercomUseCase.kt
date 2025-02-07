package com.forematic.forelock.setupdevice.domain

import android.content.Context
import android.content.IntentFilter
import android.util.Log
import com.forematic.forelock.core.domain.model.MessageListener
import com.forematic.forelock.core.utils.Constants
import com.forematic.forelock.core.utils.MessageReceiver
import com.forematic.forelock.core.utils.MessageSender
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class IntercomUseCase(
    private val context: Context,
    private val messageSender: MessageSender
): MessageListener {
    suspend fun getSignalStrength(simNumber: String, password: String): Int? =
        suspendCancellableCoroutine { continuation ->
            val messageReceiver = MessageReceiver(this)
            context.registerReceiver(messageReceiver, IntentFilter(MessageReceiver.SMS_RECEIVED_ACTION))

            try {
                messageSender.sendMessage(simNumber, password, Constants.GET_SIGNAL_STRENGTH_REQUEST)
            } catch (e: Exception) {
                Log.e("IntercomUseCase", "Error while sending SMS", e)
                continuation.resume(null)
            }
            continuation.invokeOnCancellation {
                context.unregisterReceiver(messageReceiver)
            }
        }

    override fun onMessageReceived(senderAddress: String, messageBody: String) {
        Log.d(TAG, "Received SMS: $messageBody")
        val signalStrength = extractSignalStrength(messageBody)
    }

    private fun extractSignalStrength(message: String): Int? {
        val regex = Regex("RSSI is (\\d+)")
        val matchResult = regex.find(message)
        return if (matchResult != null) {
            matchResult.groupValues[1].toIntOrNull()
        } else {
            null
        }
    }

    companion object {
        const val TAG = "IntercomUseCase"
    }
}