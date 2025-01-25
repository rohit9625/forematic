package com.forematic.forelock.core.domain.model

sealed class MessageUpdate(val requestCode: Int) {
    class Sent(requestCode: Int) : MessageUpdate(requestCode)
    class Delivered(requestCode: Int) : MessageUpdate(requestCode)
    class Error(requestCode: Int, val error: MessageError) : MessageUpdate(requestCode)
}