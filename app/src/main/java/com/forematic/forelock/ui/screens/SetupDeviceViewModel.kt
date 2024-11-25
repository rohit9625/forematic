package com.forematic.forelock.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SetupDeviceViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(NewDeviceUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(e: SetupDeviceEvent) {
        when(e) {
            is SetupDeviceEvent.DeviceTypeChanged -> {
                _uiState.update { it.copy(deviceType = e.deviceType) }
            }

            is SetupDeviceEvent.TimezoneModeChanged -> {
                _uiState.update { it.copy(timezoneMode = e.timezoneMode) }
            }

            is SetupDeviceEvent.SimNumberChanged -> {
                _uiState.update { it.copy(simNumber = e.simNumber) }
            }

            is SetupDeviceEvent.ProgrammingPasswordChanged -> {
                _uiState.update { it.copy(programmingPassword = e.password) }
            }

            is SetupDeviceEvent.OutputRelayEvent -> onOutputRelayEvent(e)

            is SetupDeviceEvent.CallOutNumberEvent -> onCallOutNumberEvent(e)
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
        }
    }

    private fun onOutputRelayEvent(e: SetupDeviceEvent.OutputRelayEvent) {
        when(e) {
            is SetupDeviceEvent.OutputRelayEvent.UpdateNameRelay1 -> {
                _uiState.update { it.copy(outputRelay1 = it.outputRelay1.copy(name = e.name)) }
            }
            is SetupDeviceEvent.OutputRelayEvent.UpdateTextRelay1 -> {
                _uiState.update { it.copy(outputRelay1 = it.outputRelay1.copy(text = e.text)) }
            }
            is SetupDeviceEvent.OutputRelayEvent.UpdateTimeRelay1 -> {
                _uiState.update { it.copy(outputRelay1 = it.outputRelay1.copy(relayTime = e.relayTime)) }
            }

            // For Intercoms with two output relays
            is SetupDeviceEvent.OutputRelayEvent.UpdateNameRelay2 -> {
                _uiState.update { it.copy(outputRelay2 = it.outputRelay2?.copy(name = e.name)) }
            }
            is SetupDeviceEvent.OutputRelayEvent.UpdateTextRelay2 -> {
                _uiState.update { it.copy(outputRelay2 = it.outputRelay2?.copy(text = e.text)) }
            }
            is SetupDeviceEvent.OutputRelayEvent.UpdateTimeRelay2 -> {
                _uiState.update { it.copy(outputRelay2 = it.outputRelay2?.copy(relayTime = e.relayTime)) }
            }
        }
    }
}