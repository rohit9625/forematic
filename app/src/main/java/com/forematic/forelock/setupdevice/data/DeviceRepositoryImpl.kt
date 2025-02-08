package com.forematic.forelock.setupdevice.data

import com.forematic.forelock.core.domain.model.MessageUpdate
import com.forematic.forelock.core.utils.Constants
import com.forematic.forelock.core.utils.MessageSender
import com.forematic.forelock.setupdevice.domain.DeviceRepository
import com.forematic.forelock.setupdevice.presentation.KeypadCodeForOutput
import kotlinx.coroutines.flow.StateFlow

class DeviceRepositoryImpl(
    private val messageSender: MessageSender
): DeviceRepository {
    override val messageUpdates: StateFlow<MessageUpdate?> by lazy {
        messageSender.messageUpdates
    }

    override fun setNewPassword(simNumber: String, oldPassword: String, newPassword: String) {
        messageSender.sendMessage(simNumber,
            "$oldPassword#PWD#$newPassword#",
            Constants.UPDATE_PASSWORD_REQUEST
        )
    }

    override fun setTimezoneMode(simNumber: String, password: String, timezoneMode: String) {
        messageSender.sendMessage(simNumber,
            "$password#$timezoneMode#",
            Constants.UPDATE_TIMEZONE_REQUEST
        )
    }

    override fun setKeypadCodes(
        simNumber: String,
        password: String,
        keypadCode1: KeypadCodeForOutput,
        keypadCode2: KeypadCodeForOutput,
        deliveryCode: KeypadCodeForOutput
    ) {
        messageSender.sendMessage(simNumber,
            "$password#11*#${keypadCode1.location}#${keypadCode1.code}#" +
                    "11*#${keypadCode2.location}#${keypadCode2.code}#" +
                    "11*#${deliveryCode.location}#${deliveryCode.code}",
            Constants.UPDATE_KEYPAD_CODES_REQUEST
        )
    }
}