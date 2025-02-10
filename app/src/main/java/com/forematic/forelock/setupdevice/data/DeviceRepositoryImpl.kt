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
        messageSender.sendMessage(
            recipientNumber = simNumber,
            messageContent = "$oldPassword#PWD#$newPassword#",
            requestCode = Constants.UPDATE_PASSWORD_REQUEST
        )
    }

    override fun setOutputNameAndRelayTime(
        simNumber: String, password: String,
        relay1OutputName: String, relay1Time: String,
        relay2OutputName: String, relay2Time: String
    ) {
        messageSender.sendMessage(
            recipientNumber = simNumber,
            messageContent = "$password#ID1#$relay1OutputName#RL1T#$relay1Time#" +
                    "ID2#$relay2OutputName#RL2T#$relay2Time#",
            requestCode = Constants.SET_OUTPUT_NAME_RELAY_TIME_REQ
        )
    }

    override fun setTimezoneMode(simNumber: String, password: String, timezoneMode: String) {
        messageSender.sendMessage(
            recipientNumber = simNumber,
            messageContent = "$password#$timezoneMode#",
            requestCode = Constants.UPDATE_TIMEZONE_REQUEST
        )
    }

    override fun setKeypadCodes(
        simNumber: String,
        password: String,
        keypadCode1: KeypadCodeForOutput,
        keypadCode2: KeypadCodeForOutput,
        deliveryCode: KeypadCodeForOutput
    ) {
        messageSender.sendMessage(
            recipientNumber = simNumber,
            messageContent = "$password#11#${keypadCode1.location}#${keypadCode1.code}#" +
                    "11#${keypadCode2.location}#${keypadCode2.code}#" +
                    "11#${deliveryCode.location}#${deliveryCode.code}",
            requestCode = Constants.UPDATE_KEYPAD_CODES_REQUEST
        )
    }

    override fun setCliMode(simNumber: String, password: String, cliMode: String) {
        messageSender.sendMessage(
            recipientNumber = simNumber,
            messageContent = "$password#$cliMode#",
            requestCode = Constants.SET_CLI_MODE_REQUEST
        )
    }

    override fun setCliNumber(
        simNumber: String,
        password: String,
        cliNumber: String,
        location: String
    ) {
        messageSender.sendMessage(
            recipientNumber = simNumber,
            messageContent = "$password#11#$location#$cliNumber#",
            requestCode = Constants.SET_CLI_NUMBER_REQUEST
        )
    }

    override fun setCallOutNumbers(
        simNumber: String,
        password: String,
        firstCallOutNumber: String,
        secondCallOutNumber: String,
        thirdCallOutNumber: String
    ) {
        messageSender.sendMessage(
            recipientNumber = simNumber,
            messageContent = "$password#01#$firstCallOutNumber#" +
                    "02#$secondCallOutNumber#03#$thirdCallOutNumber",
            requestCode = Constants.SET_CALLOUT_NUMBERS
        )
    }

    override fun setAdminNumber(simNumber: String, password: String, adminNumber: String) {
        messageSender.sendMessage(
            recipientNumber = simNumber,
            messageContent = "$password#00#$adminNumber#",
            requestCode = Constants.SET_ADMIN_NUMBER_REQUEST
        )
    }

    override fun setMicVolume(simNumber: String, password: String, volume: String) {
        messageSender.sendMessage(
            recipientNumber = simNumber,
            messageContent = "$password#MIC#$volume#",
            requestCode = Constants.SET_MIC_VOLUME_REQUEST
        )
    }

    override fun setSpeakerVolume(simNumber: String, password: String, volume: String) {
        messageSender.sendMessage(
            recipientNumber = simNumber,
            messageContent = "$password#SP#$volume#",
            requestCode = Constants.SET_SPEAKER_VOLUME_REQUEST
        )
    }
}