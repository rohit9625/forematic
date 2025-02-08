package com.forematic.forelock.core.domain.use_case

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import com.forematic.forelock.core.utils.MessageReceiver
import com.forematic.forelock.core.utils.MessageSender
import com.forematic.forelock.core.utils.PhoneNumberUtils
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class SendSmsAndGetResponse(
    private val context: Context,
    private val messageSender: MessageSender
) {
    private lateinit var messageReceiver: BroadcastReceiver

    suspend operator fun invoke(
        simNumber: String, message: String, requestCode: Int
    ): String? = suspendCancellableCoroutine { continuation ->

        messageReceiver = MessageReceiver { senderAddress, receivedMessage ->
            Log.d(TAG, "Message received from $senderAddress: $receivedMessage")
            checkReceivedMessage(continuation, simNumber, senderAddress, receivedMessage)
        }

        context.registerReceiver(messageReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))

        try {
            messageSender.sendMessage(simNumber, message, requestCode)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ", e)
            continuation.resume(null)
        }

        continuation.invokeOnCancellation {
            Log.e(TAG, "Cancelling the coroutine from $TAG", it)
            context.unregisterReceiver(messageReceiver)
        }
    }

    private fun checkReceivedMessage(
        continuation: Continuation<String?>,
        targetSimNumber: String,
        senderAddress: String?,
        receivedMessage: String?
    ) {
        if(PhoneNumberUtils.arePhoneNumbersEqual(targetSimNumber, senderAddress ?: "")) {
            continuation.resume(receivedMessage)
            context.unregisterReceiver(messageReceiver)
        }
    }

    companion object {
        private const val TAG = "SendSmsAndGetResponseUseCase"
    }
}