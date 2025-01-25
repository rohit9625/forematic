package com.forematic.forelock.core.domain.model

enum class MessageError: Error {
    GENERIC_FAILURE,
    NO_SERVICE,
    NULL_PDU,
    RADIO_OFF
}