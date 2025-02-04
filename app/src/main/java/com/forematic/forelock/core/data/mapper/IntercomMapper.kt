package com.forematic.forelock.core.data.mapper

import com.forematic.forelock.core.data.database.entity.CallOutNumberEntity
import com.forematic.forelock.core.data.database.entity.G24IntercomDetails
import com.forematic.forelock.core.data.database.entity.OutputRelayEntity
import com.forematic.forelock.setupdevice.domain.model.G24Intercom
import com.forematic.forelock.setupdevice.domain.model.CallOutNumber
import com.forematic.forelock.setupdevice.domain.model.OutputRelay
import com.forematic.forelock.setupdevice.presentation.CallerLineMode
import com.forematic.forelock.setupdevice.presentation.OutputRelayText
import com.forematic.forelock.setupdevice.presentation.TimezoneMode

fun CallOutNumberEntity.toCallOutNumber() = CallOutNumber(
    number = number,
    name = name
)

fun CallOutNumber.toEntity(intercomId: Int) = CallOutNumberEntity(
    intercomId = intercomId,
    name = name,
    number = number
)

fun OutputRelayEntity.toOutputRelay() = OutputRelay(
    outputName = outputName,
    statusText = OutputRelayText.valueOf(statusText),
    relayTime = relayTime,
    keypadCode = keypadCode,
    keypadCodeLocation = keypadCodeLocation
)

fun OutputRelay.toEntity(intercomId: Int) = OutputRelayEntity(
    intercomId = intercomId,
    outputName = outputName,
    statusText = statusText.name,
    relayTime = relayTime,
    keypadCode = keypadCode,
    keypadCodeLocation = keypadCodeLocation,
)

fun G24IntercomDetails.toIntercom() = G24Intercom(
    id = intercom.id,
    simNumber = intercom.simNumber,
    programmingPassword = intercom.programmingPassword,
    speakerVolume = intercom.speakerVolume,
    micVolume = intercom.micVolume,
    signalStrength = intercom.signalStrength,
    timezoneMode = TimezoneMode.valueOf(intercom.timezoneMode),
    adminNumber = intercom.adminNumber,
    cliMode = CallerLineMode.valueOf(intercom.cliMode),
    cliNumber = intercom.cliNumber,
    cliNumberLocation = intercom.cliNumberLocation,
    deliveryCode = intercom.deliveryCode,
    deliverCodeLocation = intercom.deliverCodeLocation,
    firstCallOutNumber = callOutNumbers.getOrNull(0)?.toCallOutNumber(),
    secondCallOutNumber = callOutNumbers.getOrNull(1)?.toCallOutNumber(),
    thirdCallOutNumber = callOutNumbers.getOrNull(2)?.toCallOutNumber(),
    firstOutputRelay = outputRelays.getOrNull(0)?.toOutputRelay(),
    secondOutputRelay = outputRelays.getOrNull(1)?.toOutputRelay(),
)