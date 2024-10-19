package com.devx.signalgsm.ui.screens

import android.telephony.SmsManager
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val smsManager: SmsManager
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(e: HomeScreenEvent) {
        when(e) {
            is HomeScreenEvent.UpdateDialogVisibility -> {
                _uiState.update { it.copy(showOpenGateDialog = e.value) }
            }
            is HomeScreenEvent.OnPinChange -> {
                if(e.pin.length <= 4) {
                    _uiState.update { it.copy(pinNumber = e.pin) }
                }
            }
            is HomeScreenEvent.OnTargetNumberChange -> {
                if(e.number.length <= 11) {
                    _uiState.update { it.copy(targetGsmNumber = e.number) }
                }
            }

            is HomeScreenEvent.OnSendSignal -> {
                val error = when {
                    isInputBlank() -> "All fields are required"
                    !isValidPin(_uiState.value.pinNumber) -> "Invalid pin number"
                    !isValidUKPhoneNumber(_uiState.value.targetGsmNumber) -> "Invalid phone number"

                    else -> null
                }

                if(error != null) {
                    _uiState.update { it.copy(errorMessage = error) }
                } else {
                    sendOpenGateSignal(_uiState.value.pinNumber, _uiState.value.targetGsmNumber)
                    e.showSnack()
                }
            }
        }
    }

    private fun sendOpenGateSignal(pinNumber: String, gsmNumber: String) {
        smsManager.sendTextMessage(gsmNumber, null, "$pinNumber#OPEN#", null, null)
        _uiState.update {
            it.copy(showOpenGateDialog = false, pinNumber = "", targetGsmNumber = "", errorMessage = null)
        }
    }

    private fun isInputBlank(): Boolean {
        return _uiState.value.pinNumber.isBlank() || _uiState.value.targetGsmNumber.isBlank()
    }

    private fun isValidPin(pin: String): Boolean {
        return pin.matches(Regex("^\\d{4}\$"))
    }

    private fun isValidUKPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.matches(Regex("^\\+44\\d{10,11}\$|^0\\d{10,11}\$"))
    }
}