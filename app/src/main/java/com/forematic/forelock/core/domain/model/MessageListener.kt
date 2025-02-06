package com.forematic.forelock.core.domain.model

interface MessageListener {
    fun onMessageReceived(senderAddress: String, messageBody: String)
}