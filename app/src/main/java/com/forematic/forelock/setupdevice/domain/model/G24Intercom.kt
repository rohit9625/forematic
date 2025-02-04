package com.forematic.forelock.setupdevice.domain.model

import com.forematic.forelock.setupdevice.presentation.CallerLineMode
import com.forematic.forelock.setupdevice.presentation.TimezoneMode

data class G24Intercom(
    val id: Int,
    val simNumber: String,
    val programmingPassword: String,
    val adminNumber: String,
    val signalStrength: Int,
    val speakerVolume: Int = 0,
    val micVolume: Int = 0,
    val timezoneMode: TimezoneMode = TimezoneMode.FREE,
    val cliMode: CallerLineMode = CallerLineMode.ANY,
    val cliNumber: String? = null,
    val cliNumberLocation: String? = null,
    val deliveryCode: String? = null,
    val deliverCodeLocation: String? = null,
    val firstCallOutNumber: CallOutNumber? = null,
    val secondCallOutNumber: CallOutNumber? = null,
    val thirdCallOutNumber: CallOutNumber? = null,
    val firstOutputRelay: OutputRelay? = null,
    val secondOutputRelay: OutputRelay? = null
)