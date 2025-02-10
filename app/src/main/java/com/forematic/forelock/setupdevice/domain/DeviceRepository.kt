package com.forematic.forelock.setupdevice.domain

import com.forematic.forelock.core.domain.model.MessageUpdate
import com.forematic.forelock.setupdevice.presentation.KeypadCodeForOutput
import kotlinx.coroutines.flow.StateFlow

interface DeviceRepository {
    val messageUpdates: StateFlow<MessageUpdate?>

    fun setNewPassword(simNumber: String, oldPassword: String, newPassword: String)

    fun setTimezoneMode(simNumber: String, password: String, timezoneMode: String)

    fun setKeypadCodes(
        simNumber: String, password: String,
        keypadCode1: KeypadCodeForOutput,
        keypadCode2: KeypadCodeForOutput,
        deliveryCode: KeypadCodeForOutput
    )

    fun setCliMode(simNumber: String, password: String, cliMode: String)

    fun setCliNumber(simNumber: String, password: String, cliNumber: String, location: String)

    fun setCallOutNumbers(
        simNumber: String, password: String,
        firstCallOutNumber: String,
        secondCallOutNumber: String,
        thirdCallOutNumber: String
    )

    fun setAdminNumber(simNumber: String, password: String, adminNumber: String)
}