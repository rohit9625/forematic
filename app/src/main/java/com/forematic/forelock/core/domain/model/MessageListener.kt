package com.forematic.forelock.core.domain.model

import kotlin.coroutines.Continuation

interface MessageListener {
    fun onMessageReceived(continuation: Continuation<String?>, senderAddress: String, messageBody: String)
}