package com.forematic.forelock.core.utils

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.forematic.forelock.core.domain.model.MessageError
import com.forematic.forelock.core.domain.model.MessageUpdate
import com.forematic.forelock.core.domain.model.PermissionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MessageSender(
    private val context: Context,
    private val smsManager: SmsManager,
    private val permissionHandler: PermissionHandler
) {
    private var sentStatusReceiver: BroadcastReceiver? = null
    private var deliveryStatusReceiver: BroadcastReceiver? = null

    private val _messageUpdates = MutableStateFlow<MessageUpdate?>(null)
    val messageUpdates: StateFlow<MessageUpdate?> = _messageUpdates.asStateFlow()

    private var pendingMessage: PendingMessage? = null

    fun sendMessage(recipientNumber: String, messageContent: String, requestCode: Int) {
        if(permissionHandler.hasPermission(Manifest.permission.SEND_SMS)) {
            sendMessageWithIntent(recipientNumber, messageContent, requestCode)
        } else {
            pendingMessage = PendingMessage(recipientNumber, messageContent, requestCode)
            permissionHandler.requestSmsPermissions()
        }
    }

    /**
    * Sends an SMS message to the specified recipient.
    *
    * @param recipientNumber The phone number of the recipient.
    * @param messageContent The text content of the SMS message.
    * @param requestCode A unique integer request code to identify this specific SMS message.
    *                    This code will be used to match the sent/delivered status to this message.
    */
    private fun sendMessageWithIntent(recipientNumber: String, messageContent: String, requestCode: Int) {
        val sentPendingIntent = PendingIntent.getBroadcast(
            context, requestCode,
            Intent(SMS_SENT_ACTION).apply { putExtra(REQUEST_CODE_EXTRA, requestCode) },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val deliveredPendingIntent = PendingIntent.getBroadcast(
            context, requestCode,
            Intent(SMS_DELIVERED_ACTION).apply { putExtra(REQUEST_CODE_EXTRA, requestCode) },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        try {
            smsManager.sendTextMessage(recipientNumber, null,
                messageContent, sentPendingIntent, deliveredPendingIntent
            )
        } catch (e: Exception) {
            Log.e("MessageSender", "Error while sending SMS", e)
            if(e is IllegalArgumentException) {
                _messageUpdates.update { MessageUpdate.Error(requestCode, MessageError.GENERIC_FAILURE) }
            }
        }
    }

    /**
     * Registers the broadcast receivers to listen for SMS sent/delivered status updates.
     *
     * @param context The application context.
     */
    fun registerBroadcastReceivers(context: Context) {
        sentStatusReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val requestCode = intent.getIntExtra(REQUEST_CODE_EXTRA, -1)

                when (resultCode) {
                    Activity.RESULT_OK -> {
                        _messageUpdates.update { MessageUpdate.Sent(requestCode) }
                    }
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                        _messageUpdates.update { MessageUpdate.Error(requestCode, MessageError.GENERIC_FAILURE) }
                    }
                    SmsManager.RESULT_ERROR_NO_SERVICE -> {
                        _messageUpdates.update { MessageUpdate.Error(requestCode, MessageError.NO_SERVICE) }
                    }
                    SmsManager.RESULT_ERROR_NULL_PDU -> {
                        _messageUpdates.update { MessageUpdate.Error(requestCode, MessageError.NULL_PDU) }
                    }
                    SmsManager.RESULT_ERROR_RADIO_OFF -> {
                        _messageUpdates.update { MessageUpdate.Error(requestCode, MessageError.RADIO_OFF) }
                    }
                }
            }
        }

        deliveryStatusReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val requestCode = intent.getIntExtra(REQUEST_CODE_EXTRA, -1)

                when (resultCode) {
                    Activity.RESULT_OK -> {
                        _messageUpdates.update { MessageUpdate.Delivered(requestCode) }
                    }
                    Activity.RESULT_CANCELED -> {
                        _messageUpdates.update { MessageUpdate.Error(requestCode, MessageError.GENERIC_FAILURE) }
                    }
                }
            }
        }

        ContextCompat.registerReceiver(
            context,
            sentStatusReceiver,
            IntentFilter(SMS_SENT_ACTION),
            ContextCompat.RECEIVER_EXPORTED
        )
        ContextCompat.registerReceiver(
            context,
            deliveryStatusReceiver,
            IntentFilter(SMS_DELIVERED_ACTION),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    /**
     * Unregister the broadcast receivers of SMS sent/delivered status updates.
     */
    fun unregisterReceivers() {
        sentStatusReceiver?.let {
            context.unregisterReceiver(it)
            sentStatusReceiver = null
        }

        deliveryStatusReceiver?.let {
            context.unregisterReceiver(it)
            deliveryStatusReceiver = null
        }
    }

    fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            pendingMessage?.let {
                sendMessageWithIntent(it.recipientNumber, it.messageContent, it.requestCode)
                pendingMessage = null
            }
        } else {
            Toast.makeText(context, "SMS permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private data class PendingMessage(
        val recipientNumber: String,
        val messageContent: String,
        val requestCode: Int
    )

    companion object {
        private const val SMS_SENT_ACTION = "com.forematic.forelock.SMS_SENT"
        private const val SMS_DELIVERED_ACTION = "com.forematic.forelock.SMS_DELIVERED"
        // This uniquely identifies the request for the PendingIntent
        private const val REQUEST_CODE_EXTRA = "request_code"
    }
}