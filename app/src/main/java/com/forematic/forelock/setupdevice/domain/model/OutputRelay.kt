package com.forematic.forelock.setupdevice.domain.model

import com.forematic.forelock.setupdevice.presentation.OutputRelayText

data class OutputRelay(
    val outputName: String,
    val statusText: OutputRelayText,
    val relayTime: Int = 5,
    val keypadCode: String? = null,
    val keypadCodeLocation: String? = null
)
