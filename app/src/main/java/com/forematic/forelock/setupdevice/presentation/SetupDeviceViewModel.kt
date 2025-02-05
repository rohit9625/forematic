package com.forematic.forelock.setupdevice.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forematic.forelock.core.domain.InputValidator
import com.forematic.forelock.core.domain.model.InputError
import com.forematic.forelock.core.domain.model.MessageUpdate
import com.forematic.forelock.core.domain.model.Result
import com.forematic.forelock.core.utils.Constants
import com.forematic.forelock.setupdevice.domain.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetupDeviceViewModel(
    private val deviceRepository: DeviceRepository,
    private val inputValidator: InputValidator
): ViewModel() {
    private val _uiState = MutableStateFlow(NewDeviceUiState(
        deviceType = DeviceType.G24_INTERCOM,
        currentProgrammingPassword = "1234",
        outputRelay1 = OutputRelay(),
        outputRelay2 = OutputRelay(),
    ))
    val uiState = _uiState.asStateFlow()

    private val stateUpdateMap: Map<Int, (MessageUpdate) -> Unit> = mapOf(
        Constants.UPDATE_PASSWORD_REQUEST to { messageUpdate: MessageUpdate ->
            updateSimAndPasswordState(messageUpdate)
        },
        Constants.UPDATE_TIMEZONE_REQUEST to { messageUpdate: MessageUpdate ->
            updateTimezoneMode(messageUpdate)
        },
        Constants.UPDATE_KEYPAD_CODES_REQUEST to { messageUpdate: MessageUpdate ->
            updateKeypadCodes(messageUpdate)
        }
        // Add more request codes and update functions here...
    )

    init {
        viewModelScope.launch {
            deviceRepository.messageUpdates.collect {
                it?.let {
                    stateUpdateMap[it.requestCode]?.invoke(it)
                }
            }
        }
    }

    fun onEvent(e: SetupDeviceEvent) {
        when(e) {
            is SetupDeviceEvent.SimAndPasswordEvent -> onSimAndPasswordEvent(e)

            is SetupDeviceEvent.DeviceTypeChanged -> {
                _uiState.update { it.copy(deviceType = e.deviceType) }
            }

            is SetupDeviceEvent.TimezoneModeEvent -> onTimezoneModeEvent(e)

            is SetupDeviceEvent.OutputRelayEvent -> onOutputRelayEvent(e)

            is SetupDeviceEvent.KeypadCodeEvent -> onKeypadCodeEvent(e)

            is SetupDeviceEvent.CallOutNumberEvent -> onCallOutNumberEvent(e)

            is SetupDeviceEvent.CallerLineIdEvent -> onCallerLineIdEvent(e)

            is SetupDeviceEvent.OnSpeakerVolumeChange -> {
                _uiState.update { it.copy(speakerVolume = e.volume) }
            }
            is SetupDeviceEvent.OnMicVolumeChange -> {
                _uiState.update { it.copy(micVolume = e.volume) }
            }
            SetupDeviceEvent.OnCheckSignalStrength -> {
                /*TODO("Query device to check signal strength")*/
            }
        }
    }

    private fun updateSimAndPasswordState(messageUpdate: MessageUpdate) {
        _uiState.update { currentState ->
            when (messageUpdate) {
                is MessageUpdate.Sent -> currentState.copy(
                    simAndPasswordState = currentState.simAndPasswordState.copy(
                        isLoading = true,
                        simNumberError = null
                    )
                )

                is MessageUpdate.Delivered -> currentState.copy(
                    simAndPasswordState = currentState.simAndPasswordState.copy(
                        isLoading = false,
                        simNumberError = null
                    ),
                    currentProgrammingPassword = uiState.value.simAndPasswordState.programmingPassword
                )

                is MessageUpdate.Error -> currentState.copy(
                    simAndPasswordState = currentState.simAndPasswordState.copy(
                        isLoading = false,
                        simNumberError = messageUpdate.error.toString()
                    )
                )
            }
        }
    }

    private fun onTimezoneModeEvent(e: SetupDeviceEvent.TimezoneModeEvent) {
        when(e) {
            is SetupDeviceEvent.TimezoneModeEvent.OnTimezoneModeChange -> {
                _uiState.update { it.copy(timezoneMode = e.timezoneMode) }
            }
            SetupDeviceEvent.TimezoneModeEvent.OnUpdateClick -> {
                deviceRepository.setTimezoneMode(
                    uiState.value.simAndPasswordState.simNumber,
                    uiState.value.currentProgrammingPassword,
                    uiState.value.timezoneMode.code
                )
            }
        }
    }

    private fun updateTimezoneMode(messageUpdate: MessageUpdate) {
        _uiState.update { currentState ->
            when(messageUpdate) {
                is MessageUpdate.Sent -> {
                    currentState.copy(isUpdatingTimezone = true)
                }
                is MessageUpdate.Delivered -> {
                    currentState.copy(isUpdatingTimezone = false)
                }
                is MessageUpdate.Error -> {
                    currentState.copy(isUpdatingTimezone = false)
                }
            }
        }
    }

    private fun updateKeypadCodes(messageUpdate: MessageUpdate) {
        _uiState.update { currentState ->
            when(messageUpdate) {
                is MessageUpdate.Sent -> {
                    currentState.copy(isUpdatingKeypadCodes = true)
                }
                is MessageUpdate.Delivered -> {
                    currentState.copy(isUpdatingKeypadCodes = false)
                }
                is MessageUpdate.Error -> {
                    currentState.copy(isUpdatingKeypadCodes = false)
                }
            }
        }
    }

    private fun onSimAndPasswordEvent(e: SetupDeviceEvent.SimAndPasswordEvent) {
        when(e) {
            is SetupDeviceEvent.SimAndPasswordEvent.OnSimNumberChange -> {
                val error = when(val result = inputValidator.validateUkPhoneNumber(e.number)) {
                    is Result.Failure -> when(result.error) {
                        InputError.PhoneNumberError.INVALID_NUMBER -> "Invalid SIM Number"
                    }
                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(simAndPasswordState = it.simAndPasswordState.copy(
                        simNumber = e.number, simNumberError = error)
                    )
                }
            }

            is SetupDeviceEvent.SimAndPasswordEvent.OnPasswordChange -> {
                val error = when(val result = inputValidator.validateProgrammingPassword(e.password)) {
                    is Result.Failure -> when(result.error) {
                        InputError.PasswordError.INVALID_LENGTH -> "Password must contain 4 characters"
                        InputError.PasswordError.INVALID_CHARS -> "Password should have numbers and letters"
                    }
                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(simAndPasswordState = it.simAndPasswordState
                        .copy(programmingPassword = e.password, passwordError = error))
                }
            }

            SetupDeviceEvent.SimAndPasswordEvent.OnUpdateClick -> {
                val numberError = if(uiState.value.simAndPasswordState.simNumber.isBlank())
                    "SIM number cannot be empty" else null
                val passwordError = if(uiState.value.currentProgrammingPassword
                == uiState.value.simAndPasswordState.programmingPassword) "Password is same as current password"
                else null

                _uiState.update { it.copy(simAndPasswordState = it.simAndPasswordState.copy(
                    simNumberError = numberError, passwordError = passwordError)) }

                if(numberError != null || passwordError != null) return

                deviceRepository.setNewPassword(
                    uiState.value.simAndPasswordState.simNumber,
                    uiState.value.currentProgrammingPassword,
                    uiState.value.simAndPasswordState.programmingPassword
                )
            }
        }
    }

    private fun onCallOutNumberEvent(e: SetupDeviceEvent.CallOutNumberEvent) {
        when(e) {
            is SetupDeviceEvent.CallOutNumberEvent.OnNameChange -> {
                _uiState.update {
                    it.copy(
                        callOutNumbers = it.callOutNumbers.mapIndexed { index, callOutNumber ->
                            if (index == e.index) callOutNumber.copy(name = e.name) else callOutNumber
                        }
                    )
                }
            }

            is SetupDeviceEvent.CallOutNumberEvent.OnNumberChange -> {
                _uiState.update {
                    it.copy(
                        callOutNumbers = it.callOutNumbers.mapIndexed { index, callOutNumber ->
                            if (index == e.index) callOutNumber.copy(number = e.number) else callOutNumber
                        }
                    )
                }
            }

            SetupDeviceEvent.CallOutNumberEvent.OnUpdateClick -> {
                /*TODO("Update all call-out numbers in single-shot")*/
            }

            is SetupDeviceEvent.CallOutNumberEvent.OnAdminNumberChange -> {
                _uiState.update { it.copy(adminNumber = e.number) }
            }
            SetupDeviceEvent.CallOutNumberEvent.OnChangeClick -> {
                /*TODO("Update admin number to the target device")*/
            }
        }
    }

    private fun onOutputRelayEvent(e: SetupDeviceEvent.OutputRelayEvent) {
        when(e) {
            is SetupDeviceEvent.OutputRelayEvent.OnRelay1NameChange -> {
                val error = when(val result = inputValidator.validateName(e.name)) {
                    is Result.Failure -> when(result.error) {
                        InputError.NameError.EMPTY -> "Enter a name for relay"
                        InputError.NameError.TOO_SHORT -> "Name is too short"
                        InputError.NameError.TOO_LONG -> "Name is too long"
                        InputError.NameError.INVALID_FORMAT -> "Invalid name format"
                    }
                    is Result.Success -> null
                }
                _uiState.update { it.copy(outputRelay1 = it.outputRelay1.copy(
                    name = e.name, outputNameError = error))
                }
            }
            is SetupDeviceEvent.OutputRelayEvent.OnRelay1TextChange -> {
                _uiState.update { it.copy(outputRelay1 = it.outputRelay1.copy(text = e.text)) }
            }
            is SetupDeviceEvent.OutputRelayEvent.OnRelay1TimeChange -> {
                _uiState.update {
                    val updatedRelay = it.outputRelay1.copy(relayTime = e.relayTime)
                    val error = if (updatedRelay.isRelayTimeInRange()) null
                    else "Relay time must be between ${updatedRelay.relayTimeRange.first}" +
                            "-${updatedRelay.relayTimeRange.last}"

                    it.copy(outputRelay1 = updatedRelay.copy(relayTimeError = error))
                }
            }
            is SetupDeviceEvent.OutputRelayEvent.OnRelay1IconChange -> {
                _uiState.update { it.copy(outputRelay1 = it.outputRelay1.copy(icon = e.icon)) }
            }
            SetupDeviceEvent.OutputRelayEvent.OnGetNameForRelay1 -> {
                /*TODO("Send SMS to device to get the output relay name")*/
            }

            // For Intercoms with two output relays
            is SetupDeviceEvent.OutputRelayEvent.OnRelay2NameChange -> {
                val error = when(val result = inputValidator.validateName(e.name)) {
                    is Result.Failure -> when(result.error) {
                        InputError.NameError.EMPTY -> "Enter a name for the relay"
                        InputError.NameError.TOO_SHORT -> "Output Name is too short"
                        InputError.NameError.TOO_LONG -> "Output Name is too long"
                        InputError.NameError.INVALID_FORMAT -> "Invalid output name format"
                    }
                    is Result.Success -> null
                }
                _uiState.update { it.copy(outputRelay2 = it.outputRelay2?.copy(
                    name = e.name, outputNameError = error))
                }
            }
            is SetupDeviceEvent.OutputRelayEvent.OnRelay2TextChange -> {
                _uiState.update { it.copy(outputRelay2 = it.outputRelay2?.copy(text = e.text)) }
            }
            is SetupDeviceEvent.OutputRelayEvent.OnRelay2TimeChange -> {
                _uiState.update {
                    val updatedRelay = it.outputRelay2!!.copy(relayTime = e.relayTime)
                    val error = if (updatedRelay.isRelayTimeInRange()) null
                    else "Relay time must be between ${updatedRelay.relayTimeRange.first}" +
                            "-${updatedRelay.relayTimeRange.last}"

                    it.copy(outputRelay2 = updatedRelay.copy(relayTimeError = error))
                }
            }
            is SetupDeviceEvent.OutputRelayEvent.OnRelay2IconChange -> {
                _uiState.update { it.copy(outputRelay2 = it.outputRelay2?.copy(icon = e.icon)) }
            }
            SetupDeviceEvent.OutputRelayEvent.OnGetNameForRelay2 -> {
                /*TODO("Send SMS to device to get the output relay name")*/
            }

            SetupDeviceEvent.OutputRelayEvent.OnUpdateClick -> {
                _uiState.update {
                    val relay1Error = if(it.outputRelay1.isAnyInputEmpty())
                        "Please configure relay completely" else null
                    val relay2Error = if(it.outputRelay2!!.isAnyInputEmpty())
                        "Please configure relay completely" else null

                    it.copy(outputRelay1 = it.outputRelay1.copy(otherError = relay1Error),
                        outputRelay2 = it.outputRelay2.copy(otherError = relay2Error))
                }
            }
        }
    }

    private fun onKeypadCodeEvent(e: SetupDeviceEvent.KeypadCodeEvent) {
        when(e) {
            is SetupDeviceEvent.KeypadCodeEvent.OnKeypadCode1Change -> {
                _uiState.update { it.copy(keypadCode1 = it.keypadCode1.copy(code = e.code)) }
            }
            is SetupDeviceEvent.KeypadCodeEvent.OnCodeLocation1Change -> {
                _uiState.update { it.copy(keypadCode1 = it.keypadCode1.copy(location = e.location)) }
            }
            SetupDeviceEvent.KeypadCodeEvent.OnFindKeypadCode1Location -> {
                /*TODO("Query Intercom to receive next available location")*/
            }

            is SetupDeviceEvent.KeypadCodeEvent.OnKeypadCode2Change -> {
                _uiState.update { it.copy(keypadCode2 = it.keypadCode2.copy(code = e.code)) }
            }
            is SetupDeviceEvent.KeypadCodeEvent.OnCodeLocation2Change -> {
                _uiState.update { it.copy(keypadCode2 = it.keypadCode2.copy(location = e.location)) }
            }
            SetupDeviceEvent.KeypadCodeEvent.OnFindKeypadCode2Location -> {
                /*TODO("Query Intercom to receive next available location")*/
            }

            is SetupDeviceEvent.KeypadCodeEvent.OnDeliveryCodeChange -> {
                _uiState.update { it.copy(deliveryCode = it.deliveryCode.copy(code = e.code)) }
            }
            is SetupDeviceEvent.KeypadCodeEvent.OnDeliveryCodeLocationChange -> {
                _uiState.update { it.copy(deliveryCode = it.deliveryCode.copy(location = e.location)) }
            }
            SetupDeviceEvent.KeypadCodeEvent.OnFindDeliveryCodeLocation -> {
                /*TODO("Query Intercom to receive next available location")*/
            }

            SetupDeviceEvent.KeypadCodeEvent.OnUpdateClick -> {
                deviceRepository.setKeypadCodes(
                    simNumber = uiState.value.simAndPasswordState.simNumber,
                    password = uiState.value.currentProgrammingPassword,
                    keypadCode1 = uiState.value.keypadCode1,
                    keypadCode2 = uiState.value.keypadCode2,
                    deliveryCode = uiState.value.deliveryCode
                )
            }
        }

    }

    private fun onCallerLineIdEvent(e: SetupDeviceEvent.CallerLineIdEvent) {
        when(e) {
            is SetupDeviceEvent.CallerLineIdEvent.OnUserModeChange -> {
                _uiState.update { it.copy(callerLineId = it.callerLineId.copy(userMode = e.userMode)) }
            }
            is SetupDeviceEvent.CallerLineIdEvent.OnNumberChange -> {
                _uiState.update { it.copy(callerLineId = it.callerLineId.copy(number = e.number)) }
            }
            is SetupDeviceEvent.CallerLineIdEvent.OnLocationChange -> {
                _uiState.update { it.copy(callerLineId = it.callerLineId.copy(location = e.location)) }
            }
            SetupDeviceEvent.CallerLineIdEvent.OnFindLocation -> {
                /*TODO()*/
            }

            SetupDeviceEvent.CallerLineIdEvent.OnUpdateClick -> {
                /*TODO("Update information to the target device")*/
            }
        }
    }
}