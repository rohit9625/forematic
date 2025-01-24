package com.forematic.forelock.setupdevice.presentation

import androidx.lifecycle.ViewModel
import com.forematic.forelock.core.domain.InputValidator
import com.forematic.forelock.core.domain.model.InputError
import com.forematic.forelock.core.domain.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SetupDeviceViewModel(
    private val inputValidator: InputValidator
): ViewModel() {
    private val _uiState = MutableStateFlow(NewDeviceUiState(
        deviceType = DeviceType.G24_INTERCOM,
        simAndPasswordState = SimAndPasswordState(programmingPassword = "CCCC"),
        outputRelay1 = OutputRelay(),
        outputRelay2 = OutputRelay(),
    ))
    val uiState = _uiState.asStateFlow()

    fun onEvent(e: SetupDeviceEvent) {
        when(e) {
            is SetupDeviceEvent.SimAndPasswordEvent -> onSimAndPasswordEvent(e)

            is SetupDeviceEvent.DeviceTypeChanged -> {
                _uiState.update { it.copy(deviceType = e.deviceType) }
            }

            is SetupDeviceEvent.TimezoneModeChanged -> {
                _uiState.update { it.copy(timezoneMode = e.timezoneMode) }
            }

            is SetupDeviceEvent.OnTimezoneModeUpdate -> {
                /*Send SMS to target device to update timezone mode*/
            }

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

    private fun onSimAndPasswordEvent(e: SetupDeviceEvent.SimAndPasswordEvent) {
        when(e) {
            is SetupDeviceEvent.SimAndPasswordEvent.OnSimNumberChange -> {
                _uiState.update {
                    it.copy(simAndPasswordState = it.simAndPasswordState.copy(simNumber = e.number))
                }
            }
            is SetupDeviceEvent.SimAndPasswordEvent.OnPasswordChange -> {
                val error = when(val result = inputValidator.validateProgrammingPassword(e.password)) {
                    is Result.Failure -> when(result.error) {
                        InputError.PasswordError.INVALID_LENGTH -> "Programming password must be 4 characters"
                        InputError.PasswordError.INVALID_CHARS -> ""
                    }
                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(simAndPasswordState = it.simAndPasswordState
                        .copy(programmingPassword = e.password, error = error))
                }
            }
            SetupDeviceEvent.SimAndPasswordEvent.OnUpdateClick -> {
                
                TODO("Update the Sim and Password to device")
            }
        }
    }

    private fun onCallOutNumberEvent(e: SetupDeviceEvent.CallOutNumberEvent) {
        when(e) {
            is SetupDeviceEvent.CallOutNumberEvent.UpdateName -> {
                _uiState.update {
                    it.copy(
                        callOutNumbers = it.callOutNumbers.mapIndexed { index, callOutNumber ->
                            if (index == e.index) callOutNumber.copy(name = e.name) else callOutNumber
                        }
                    )
                }
            }

            is SetupDeviceEvent.CallOutNumberEvent.UpdateNumber -> {
                _uiState.update {
                    it.copy(
                        callOutNumbers = it.callOutNumbers.mapIndexed { index, callOutNumber ->
                            if (index == e.index) callOutNumber.copy(number = e.number) else callOutNumber
                        }
                    )
                }
            }

            SetupDeviceEvent.CallOutNumberEvent.AddMoreNumber -> {
                _uiState.update { it.copy(callOutNumbers = it.callOutNumbers + CallOutNumber()) }
            }

            SetupDeviceEvent.CallOutNumberEvent.OnUpdateClick -> {
                /*TODO("Update all call-out numbers in single-shot")*/
            }
        }
    }

    private fun onOutputRelayEvent(e: SetupDeviceEvent.OutputRelayEvent) {
        when(e) {
            is SetupDeviceEvent.OutputRelayEvent.OnRelay1NameChange -> {
                _uiState.update { it.copy(outputRelay1 = it.outputRelay1.copy(name = e.name)) }
            }
            is SetupDeviceEvent.OutputRelayEvent.OnRelay1TextChange -> {
                _uiState.update { it.copy(outputRelay1 = it.outputRelay1.copy(text = e.text)) }
            }
            is SetupDeviceEvent.OutputRelayEvent.OnRelay1TimeChange -> {
                _uiState.update { it.copy(outputRelay1 = it.outputRelay1.copy(relayTime = e.relayTime)) }
            }
            SetupDeviceEvent.OutputRelayEvent.OnGetNameForRelay1 -> {
                /*TODO("Send SMS to device to get the output relay name")*/
            }

            // For Intercoms with two output relays
            is SetupDeviceEvent.OutputRelayEvent.OnRelay2NameChange -> {
                _uiState.update { it.copy(outputRelay2 = it.outputRelay2?.copy(name = e.name)) }
            }
            is SetupDeviceEvent.OutputRelayEvent.OnRelay2TextChange -> {
                _uiState.update { it.copy(outputRelay2 = it.outputRelay2?.copy(text = e.text)) }
            }
            is SetupDeviceEvent.OutputRelayEvent.OnRelay2TimeChange -> {
                _uiState.update { it.copy(outputRelay2 = it.outputRelay2?.copy(relayTime = e.relayTime)) }
            }
            SetupDeviceEvent.OutputRelayEvent.OnGetNameForRelay2 -> {
                /*TODO("Send SMS to device to get the output relay name")*/
            }

            SetupDeviceEvent.OutputRelayEvent.OnUpdateClick -> {
                /*TODO("Update the information on target device")*/
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
                /*TODO("Save keypad codes to the target device")*/
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
        }
    }
}