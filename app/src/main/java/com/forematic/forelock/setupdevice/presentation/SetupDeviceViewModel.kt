package com.forematic.forelock.setupdevice.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forematic.forelock.core.domain.InputValidator
import com.forematic.forelock.core.domain.model.InputError
import com.forematic.forelock.core.domain.model.MessageUpdate
import com.forematic.forelock.core.domain.model.Result
import com.forematic.forelock.core.utils.Constants
import com.forematic.forelock.core.utils.SnackbarController
import com.forematic.forelock.core.utils.SnackbarEvent
import com.forematic.forelock.setupdevice.domain.DeviceRepository
import com.forematic.forelock.setupdevice.domain.use_case.FindNextLocation
import com.forematic.forelock.setupdevice.domain.use_case.GetOutputName
import com.forematic.forelock.setupdevice.domain.use_case.GetSignalStrength
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetupDeviceViewModel(
    private val deviceRepository: DeviceRepository,
    private val inputValidator: InputValidator,
    private val getSignalStrength: GetSignalStrength,
    private val findNextLocation: FindNextLocation,
    private val getOutputName: GetOutputName
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        NewDeviceUiState(
            deviceType = DeviceType.G24_INTERCOM,
            currentProgrammingPassword = "1234",
            outputRelay1 = OutputRelay(),
            outputRelay2 = OutputRelay(),
        )
    )
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
        },
        Constants.SET_CLI_MODE_REQUEST to { messageUpdate: MessageUpdate ->
            updateCallerLineState(messageUpdate)
        },
        Constants.SET_CLI_NUMBER_REQUEST to { messageUpdate: MessageUpdate ->
            updateCallerLineState(messageUpdate)
        },
        Constants.SET_CALLOUT_NUMBERS to { messageUpdate: MessageUpdate ->
            updateCallOutNumberState(messageUpdate)
        },
        Constants.SET_ADMIN_NUMBER_REQUEST to { messageUpdate: MessageUpdate ->
            updateCallOutNumberState(messageUpdate)
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
        when (e) {
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
                executeIfValidSimNumber {
                    _uiState.update { it.copy(isRefreshingSignal = true) }
                    viewModelScope.launch {
                        val signal = getSignalStrength.invoke(
                            simNumber = uiState.value.simAndPasswordState.simNumber,
                            password = uiState.value.currentProgrammingPassword,
                            requestCode = Constants.GET_SIGNAL_STRENGTH_REQUEST
                        )

                        _uiState.update {
                            it.copy(
                                signalStrength = signal,
                                isRefreshingSignal = false
                            )
                        }
                    }
                }
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

                is MessageUpdate.Received -> TODO()
            }
        }
    }

    private fun onTimezoneModeEvent(e: SetupDeviceEvent.TimezoneModeEvent) {
        when (e) {
            is SetupDeviceEvent.TimezoneModeEvent.OnTimezoneModeChange -> {
                _uiState.update { it.copy(timezoneMode = e.timezoneMode) }
            }

            SetupDeviceEvent.TimezoneModeEvent.OnUpdateClick -> {
                executeIfValidSimNumber {
                    if (uiState.value.timezoneMode == uiState.value.currentTimezoneMode) {
                        _uiState.update { it.copy(timezoneError = "Please select a different mode") }
                    } else {
                        _uiState.update { it.copy(isUpdatingTimezone = true, timezoneError = null) }
                        deviceRepository.setTimezoneMode(
                            uiState.value.simAndPasswordState.simNumber,
                            uiState.value.currentProgrammingPassword,
                            uiState.value.timezoneMode.code
                        )
                    }
                }
            }
        }
    }

    private fun updateTimezoneMode(messageUpdate: MessageUpdate) {
        _uiState.update { currentState ->
            when (messageUpdate) {
                is MessageUpdate.Sent -> {
                    currentState.copy(isUpdatingTimezone = true)
                }

                is MessageUpdate.Delivered -> {
                    currentState.copy(
                        isUpdatingTimezone = false,
                        currentTimezoneMode = uiState.value.timezoneMode
                    )
                }

                is MessageUpdate.Error -> {
                    currentState.copy(isUpdatingTimezone = false)
                }

                is MessageUpdate.Received -> TODO()
            }
        }
    }

    private fun updateKeypadCodes(messageUpdate: MessageUpdate) {
        _uiState.update { currentState ->
            when (messageUpdate) {
                is MessageUpdate.Sent -> {
                    currentState.copy(isUpdatingKeypadCodes = true)
                }

                is MessageUpdate.Delivered -> {
                    currentState.copy(isUpdatingKeypadCodes = false)
                }

                is MessageUpdate.Error -> {
                    currentState.copy(isUpdatingKeypadCodes = false)
                }

                is MessageUpdate.Received -> TODO()
            }
        }
    }

    private fun onSimAndPasswordEvent(e: SetupDeviceEvent.SimAndPasswordEvent) {
        when (e) {
            is SetupDeviceEvent.SimAndPasswordEvent.OnSimNumberChange -> {
                val error = when (val result = inputValidator.validateUkPhoneNumber(e.number)) {
                    is Result.Failure -> when (result.error) {
                        InputError.PhoneNumberError.INVALID_NUMBER -> "Invalid SIM Number"
                        InputError.PhoneNumberError.EMPTY -> "SIM number cannot be empty"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        simAndPasswordState = it.simAndPasswordState.copy(
                            simNumber = e.number, simNumberError = error
                        )
                    )
                }
            }

            is SetupDeviceEvent.SimAndPasswordEvent.OnPasswordChange -> {
                val error =
                    when (val result = inputValidator.validateProgrammingPassword(e.password)) {
                        is Result.Failure -> when (result.error) {
                            InputError.PasswordError.INVALID_LENGTH -> "Password must contain 4 characters"
                            InputError.PasswordError.INVALID_CHARS -> "Password should have numbers and letters"
                        }

                        is Result.Success -> null
                    }
                _uiState.update {
                    it.copy(
                        simAndPasswordState = it.simAndPasswordState
                            .copy(programmingPassword = e.password, passwordError = error)
                    )
                }
            }

            SetupDeviceEvent.SimAndPasswordEvent.OnUpdateClick -> {
                val numberError = if (uiState.value.simAndPasswordState.simNumber.isBlank())
                    "SIM number cannot be empty" else null
                val passwordError = if (uiState.value.currentProgrammingPassword
                    == uiState.value.simAndPasswordState.programmingPassword
                ) "Password is same as current password"
                else null

                _uiState.update {
                    it.copy(
                        simAndPasswordState = it.simAndPasswordState.copy(
                            simNumberError = numberError, passwordError = passwordError
                        )
                    )
                }

                if (numberError != null || passwordError != null) return

                deviceRepository.setNewPassword(
                    uiState.value.simAndPasswordState.simNumber,
                    uiState.value.currentProgrammingPassword,
                    uiState.value.simAndPasswordState.programmingPassword
                )
            }
        }
    }

    private fun executeIfValidSimNumber(action: () -> Unit) {
        when (val result =
            inputValidator.validateUkPhoneNumber(uiState.value.simAndPasswordState.simNumber)) {
            is Result.Failure -> when (result.error) {
                InputError.PhoneNumberError.EMPTY -> showSnackbar("SIM number cannot be empty")
                InputError.PhoneNumberError.INVALID_NUMBER -> showSnackbar("Invalid SIM number")
            }

            is Result.Success -> action()
        }
    }

    private fun showSnackbar(message: String) {
        viewModelScope.launch {
            SnackbarController.sendEvent(SnackbarEvent(message = message))
        }
    }

    private fun updateCallOutNumberState(update: MessageUpdate) {
        when (update) {
            is MessageUpdate.Sent -> {
                if (update.requestCode == Constants.SET_CALLOUT_NUMBERS) {
                    _uiState.update { it.copy(isUpdatingCallOutNumbers = true) }
                } else {
                    _uiState.update { it.copy(isUpdatingAdminNumber = true) }
                }
            }

            is MessageUpdate.Delivered -> {
                if (update.requestCode == Constants.SET_CALLOUT_NUMBERS) {
                    _uiState.update { it.copy(isUpdatingCallOutNumbers = false) }
                } else {
                    _uiState.update { it.copy(isUpdatingAdminNumber = false) }
                }
            }

            is MessageUpdate.Error -> {
                showSnackbar(message = "Unable to send command message")
                if (update.requestCode == Constants.SET_CALLOUT_NUMBERS) {
                    _uiState.update { it.copy(isUpdatingCallOutNumbers = false) }
                } else {
                    _uiState.update { it.copy(isUpdatingAdminNumber = false) }
                }
            }

            is MessageUpdate.Received -> TODO()
        }
    }

    private fun onCallOutNumberEvent(e: SetupDeviceEvent.CallOutNumberEvent) {
        when (e) {
            is SetupDeviceEvent.CallOutNumberEvent.OnFirstNameChange -> {
                val error = when (val result = inputValidator.validateName(e.name)) {
                    is Result.Failure -> when (result.error) {
                        InputError.NameError.EMPTY -> "Name cannot be empty"
                        InputError.NameError.TOO_SHORT -> "Name is too short"
                        InputError.NameError.TOO_LONG -> "Name is too long"
                        InputError.NameError.INVALID_FORMAT -> "Name can have only letters"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        firstCallOut = it.firstCallOut.copy(
                            name = e.name, nameError = error
                        )
                    )
                }
            }

            is SetupDeviceEvent.CallOutNumberEvent.OnFirstNumberChange -> {
                val error = when (val result = inputValidator.validateUkPhoneNumber(e.number)) {
                    is Result.Failure -> when (result.error) {
                        InputError.PhoneNumberError.EMPTY -> "Call-out number cannot be empty"
                        InputError.PhoneNumberError.INVALID_NUMBER -> "Invalid call-out number"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        firstCallOut = it.firstCallOut.copy(
                            number = e.number, numberError = error
                        )
                    )
                }
            }

            is SetupDeviceEvent.CallOutNumberEvent.OnSecondNameChange -> {
                val error = when (val result = inputValidator.validateName(e.name)) {
                    is Result.Failure -> when (result.error) {
                        InputError.NameError.EMPTY -> "Name cannot be empty"
                        InputError.NameError.TOO_SHORT -> "Name is too short"
                        InputError.NameError.TOO_LONG -> "Name is too long"
                        InputError.NameError.INVALID_FORMAT -> "Name can have only letters"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        secondCallOut = it.secondCallOut.copy(
                            name = e.name, nameError = error
                        )
                    )
                }
            }

            is SetupDeviceEvent.CallOutNumberEvent.OnSecondNumberChange -> {
                val error = when (val result = inputValidator.validateUkPhoneNumber(e.number)) {
                    is Result.Failure -> when (result.error) {
                        InputError.PhoneNumberError.EMPTY -> "Call-out number cannot be empty"
                        InputError.PhoneNumberError.INVALID_NUMBER -> "Invalid call-out number"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        secondCallOut = it.secondCallOut.copy(
                            number = e.number, numberError = error
                        )
                    )
                }
            }

            is SetupDeviceEvent.CallOutNumberEvent.OnThirdNameChange -> {
                val error = when (val result = inputValidator.validateName(e.name)) {
                    is Result.Failure -> when (result.error) {
                        InputError.NameError.EMPTY -> "Name cannot be empty"
                        InputError.NameError.TOO_SHORT -> "Name is too short"
                        InputError.NameError.TOO_LONG -> "Name is too long"
                        InputError.NameError.INVALID_FORMAT -> "Name can have only letters"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        thirdCallOut = it.thirdCallOut.copy(
                            name = e.name, nameError = error
                        )
                    )
                }
            }

            is SetupDeviceEvent.CallOutNumberEvent.OnThirdNumberChange -> {
                val error = when (val result = inputValidator.validateUkPhoneNumber(e.number)) {
                    is Result.Failure -> when (result.error) {
                        InputError.PhoneNumberError.EMPTY -> "Call-out number cannot be empty"
                        InputError.PhoneNumberError.INVALID_NUMBER -> "Invalid call-out number"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        thirdCallOut = it.thirdCallOut.copy(
                            number = e.number, numberError = error
                        )
                    )
                }
            }

            SetupDeviceEvent.CallOutNumberEvent.OnUpdateClick -> {
                executeIfValidSimNumber {
                    _uiState.update { it.copy(isUpdatingCallOutNumbers = true) }
                    deviceRepository.setCallOutNumbers(
                        simNumber = uiState.value.simAndPasswordState.simNumber,
                        password = uiState.value.currentProgrammingPassword,
                        firstCallOutNumber = uiState.value.firstCallOut.number,
                        secondCallOutNumber = uiState.value.secondCallOut.number,
                        thirdCallOutNumber = uiState.value.thirdCallOut.number
                    )
                }
            }

            is SetupDeviceEvent.CallOutNumberEvent.OnAdminNumberChange -> {
                val error = when (val result = inputValidator.validateUkPhoneNumber(e.number)) {
                    is Result.Failure -> when (result.error) {
                        InputError.PhoneNumberError.EMPTY -> "Admin number cannot be empty"
                        InputError.PhoneNumberError.INVALID_NUMBER -> "Invalid admin number"
                    }

                    is Result.Success -> null
                }
                _uiState.update { it.copy(adminNumber = e.number, adminNumberError = error) }
            }

            SetupDeviceEvent.CallOutNumberEvent.OnChangeClick -> {
                executeIfValidSimNumber {
                    _uiState.update { it.copy(isUpdatingAdminNumber = true) }
                    deviceRepository.setAdminNumber(
                        simNumber = uiState.value.simAndPasswordState.simNumber,
                        password = uiState.value.currentProgrammingPassword,
                        adminNumber = uiState.value.adminNumber
                    )
                }
            }
        }
    }

    private fun onOutputRelayEvent(e: SetupDeviceEvent.OutputRelayEvent) {
        when (e) {
            is SetupDeviceEvent.OutputRelayEvent.OnRelay1NameChange -> {
                val error = when (val result = inputValidator.validateName(e.name)) {
                    is Result.Failure -> when (result.error) {
                        InputError.NameError.EMPTY -> "Enter a name for relay"
                        InputError.NameError.TOO_SHORT -> "Name is too short"
                        InputError.NameError.TOO_LONG -> "Name is too long"
                        InputError.NameError.INVALID_FORMAT -> "Invalid name format"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        outputRelay1 = it.outputRelay1.copy(
                            name = e.name, outputNameError = error
                        )
                    )
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
                _uiState.update {
                    it.copy(outputRelay1 = it.outputRelay1.copy(isFetchingOutputName = true))
                }
                viewModelScope.launch {
                    val outputName = getOutputName.invoke(
                        simNumber = uiState.value.simAndPasswordState.simNumber,
                        password = uiState.value.currentProgrammingPassword,
                        requestCode = Constants.GET_R1_OUTPUT_NAME_REQUEST
                    )
                    _uiState.update {
                        it.copy(
                            outputRelay1 = it.outputRelay1.copy(
                                isFetchingOutputName = false, name = outputName ?: ""
                            )
                        )
                    }
                }
            }

            // For Intercoms with two output relays
            is SetupDeviceEvent.OutputRelayEvent.OnRelay2NameChange -> {
                val error = when (val result = inputValidator.validateName(e.name)) {
                    is Result.Failure -> when (result.error) {
                        InputError.NameError.EMPTY -> "Enter a name for the relay"
                        InputError.NameError.TOO_SHORT -> "Output Name is too short"
                        InputError.NameError.TOO_LONG -> "Output Name is too long"
                        InputError.NameError.INVALID_FORMAT -> "Invalid output name format"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        outputRelay2 = it.outputRelay2?.copy(
                            name = e.name, outputNameError = error
                        )
                    )
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
                _uiState.update {
                    it.copy(outputRelay2 = it.outputRelay2?.copy(isFetchingOutputName = true))
                }
                viewModelScope.launch {
                    val outputName = getOutputName.invoke(
                        simNumber = uiState.value.simAndPasswordState.simNumber,
                        password = uiState.value.currentProgrammingPassword,
                        requestCode = Constants.GET_R2_OUTPUT_NAME_REQUEST
                    )
                    _uiState.update {
                        it.copy(
                            outputRelay2 = it.outputRelay2?.copy(
                                isFetchingOutputName = false, name = outputName ?: ""
                            )
                        )
                    }
                }
            }

            SetupDeviceEvent.OutputRelayEvent.OnUpdateClick -> {
                _uiState.update {
                    val relay1Error = if (it.outputRelay1.isAnyInputEmpty())
                        "Please configure relay completely" else null
                    val relay2Error = if (it.outputRelay2!!.isAnyInputEmpty())
                        "Please configure relay completely" else null

                    it.copy(
                        outputRelay1 = it.outputRelay1.copy(otherError = relay1Error),
                        outputRelay2 = it.outputRelay2.copy(otherError = relay2Error)
                    )
                }
            }
        }
    }

    private fun onKeypadCodeEvent(e: SetupDeviceEvent.KeypadCodeEvent) {
        when (e) {
            is SetupDeviceEvent.KeypadCodeEvent.OnKeypadCode1Change -> {
                val error = when (val result = inputValidator.validateKeypadCode(e.code)) {
                    is Result.Failure -> when (result.error) {
                        InputError.KeypadCodeError.EMPTY -> "Keypad code cannot be empty"
                        InputError.KeypadCodeError.INVALID_LENGTH -> "Keypad code must 2-8 digits long"
                        InputError.KeypadCodeError.INVALID_FORMAT -> "Keypad code must be numbers only"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(keypadCode1 = it.keypadCode1.copy(code = e.code, codeError = error))
                }
            }

            is SetupDeviceEvent.KeypadCodeEvent.OnCodeLocation1Change -> {
                val error = when (val result = inputValidator.validateLocationInRange(
                    e.location,
                    uiState.value.keypadCode1.locationRange
                )) {
                    is Result.Failure -> when (result.error) {
                        InputError.LocationError.EMPTY -> "Location cannot be empty"
                        InputError.LocationError.OUT_OF_RANGE -> "Location must be between ${uiState.value.keypadCode1.formatedLocationRange()}"
                        InputError.LocationError.INVALID_FORMAT -> "Invalid location format"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        keypadCode1 = it.keypadCode1.copy(
                            location = e.location,
                            locationError = error
                        )
                    )
                }
            }

            SetupDeviceEvent.KeypadCodeEvent.OnFindKeypadCode1Location -> {
                executeIfValidSimNumber {
                    _uiState.update { it.copy(keypadCode1 = it.keypadCode1.copy(isFetchingLocation = true)) }
                    viewModelScope.launch {
                        val location = requestNextLocation(Constants.FIND_R1_LOCATION_REQUEST)
                        _uiState.update {
                            it.copy(
                                keypadCode1 = it.keypadCode1.copy(
                                    isFetchingLocation = false, location = location ?: ""
                                )
                            )
                        }
                    }
                }
            }

            is SetupDeviceEvent.KeypadCodeEvent.OnKeypadCode2Change -> {
                val error = when (val result = inputValidator.validateKeypadCode(e.code)) {
                    is Result.Failure -> when (result.error) {
                        InputError.KeypadCodeError.EMPTY -> "Keypad code cannot be empty"
                        InputError.KeypadCodeError.INVALID_LENGTH -> "Keypad code must 2-8 digits long"
                        InputError.KeypadCodeError.INVALID_FORMAT -> "Keypad code must be numbers only"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(keypadCode2 = it.keypadCode2.copy(code = e.code, codeError = error))
                }
            }

            is SetupDeviceEvent.KeypadCodeEvent.OnCodeLocation2Change -> {
                val error = when (val result = inputValidator.validateLocationInRange(
                    e.location,
                    uiState.value.keypadCode2.locationRange
                )) {
                    is Result.Failure -> when (result.error) {
                        InputError.LocationError.EMPTY -> "Location cannot be empty"
                        InputError.LocationError.OUT_OF_RANGE -> "Location must be between ${uiState.value.keypadCode2.formatedLocationRange()}"
                        InputError.LocationError.INVALID_FORMAT -> "Invalid location format"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        keypadCode2 = it.keypadCode2.copy(
                            location = e.location,
                            locationError = error
                        )
                    )
                }
            }

            SetupDeviceEvent.KeypadCodeEvent.OnFindKeypadCode2Location -> {
                executeIfValidSimNumber {
                    _uiState.update { it.copy(keypadCode2 = it.keypadCode2.copy(isFetchingLocation = true)) }
                    viewModelScope.launch {
                        val location = requestNextLocation(Constants.FIND_R2_LOCATION_REQUEST)
                        _uiState.update {
                            it.copy(
                                keypadCode2 = it.keypadCode2.copy(
                                    location = location ?: "", isFetchingLocation = false
                                )
                            )
                        }
                    }
                }
            }

            is SetupDeviceEvent.KeypadCodeEvent.OnDeliveryCodeChange -> {
                val error = when (val result = inputValidator.validateKeypadCode(e.code)) {
                    is Result.Failure -> when (result.error) {
                        InputError.KeypadCodeError.EMPTY -> "Keypad code cannot be empty"
                        InputError.KeypadCodeError.INVALID_LENGTH -> "Keypad code must 2-8 digits long"
                        InputError.KeypadCodeError.INVALID_FORMAT -> "Keypad code must be numbers only"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(deliveryCode = it.deliveryCode.copy(code = e.code, codeError = error))
                }
            }

            is SetupDeviceEvent.KeypadCodeEvent.OnDeliveryCodeLocationChange -> {
                val error = when (val result = inputValidator.validateLocationInRange(
                    e.location,
                    uiState.value.deliveryCode.locationRange
                )) {
                    is Result.Failure -> when (result.error) {
                        InputError.LocationError.EMPTY -> "Location cannot be empty"
                        InputError.LocationError.OUT_OF_RANGE -> "Location must be between ${uiState.value.deliveryCode.formatedLocationRange()}"
                        InputError.LocationError.INVALID_FORMAT -> "Invalid location format"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        deliveryCode = it.deliveryCode.copy(
                            location = e.location,
                            locationError = error
                        )
                    )
                }
            }

            SetupDeviceEvent.KeypadCodeEvent.OnFindDeliveryCodeLocation -> {
                executeIfValidSimNumber {
                    _uiState.update { it.copy(deliveryCode = it.deliveryCode.copy(isFetchingLocation = true)) }
                    viewModelScope.launch {
                        val location = requestNextLocation(Constants.FIND_SU_CODE_LOCATION_REQ)
                        _uiState.update {
                            it.copy(
                                deliveryCode = it.deliveryCode.copy(
                                    isFetchingLocation = false, location = location ?: ""
                                )
                            )
                        }
                    }
                }
            }

            SetupDeviceEvent.KeypadCodeEvent.OnUpdateClick -> {
                executeIfValidSimNumber {
                    _uiState.update { it.copy(isUpdatingKeypadCodes = true) }
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
    }

    private suspend fun requestNextLocation(requestCode: Int): String? {
        return findNextLocation.invoke(
            simNumber = uiState.value.simAndPasswordState.simNumber,
            password = uiState.value.currentProgrammingPassword,
            requestCode = requestCode
        )
    }

    private fun updateCallerLineState(messageUpdate: MessageUpdate) {
        _uiState.update { state ->
            when (messageUpdate) {
                is MessageUpdate.Sent -> {
                    if (messageUpdate.requestCode == Constants.SET_CLI_MODE_REQUEST) {
                        state.copy(callerLineId = state.callerLineId.copy(isUpdatingMode = true))
                    } else {
                        state.copy(callerLineId = state.callerLineId.copy(isUpdatingNumber = true))
                    }
                }

                is MessageUpdate.Delivered -> {
                    if (messageUpdate.requestCode == Constants.SET_CLI_MODE_REQUEST) {
                        state.copy(
                            callerLineId = state.callerLineId.copy(
                                isUpdatingMode = false,
                                currentUserMode = state.callerLineId.userMode
                            )
                        )
                    } else {
                        state.copy(callerLineId = state.callerLineId.copy(isUpdatingNumber = false))
                    }
                }

                is MessageUpdate.Error -> {
                    showSnackbar("Unable to send command message")
                    if (messageUpdate.requestCode == Constants.SET_CLI_MODE_REQUEST) {
                        state.copy(callerLineId = state.callerLineId.copy(isUpdatingMode = false))
                    } else {
                        state.copy(callerLineId = state.callerLineId.copy(isUpdatingNumber = false))
                    }
                }

                is MessageUpdate.Received -> TODO()
            }
        }
    }

    private fun onCallerLineIdEvent(e: SetupDeviceEvent.CallerLineIdEvent) {
        when (e) {
            is SetupDeviceEvent.CallerLineIdEvent.OnUserModeChange -> {
                _uiState.update { it.copy(callerLineId = it.callerLineId.copy(userMode = e.userMode)) }
            }

            is SetupDeviceEvent.CallerLineIdEvent.OnNumberChange -> {
                val error = when (val result = inputValidator.validateCliNumber(e.number)) {
                    is Result.Failure -> when (result.error) {
                        InputError.PhoneNumberError.EMPTY -> "Call-In number cannot be empty"
                        InputError.PhoneNumberError.INVALID_NUMBER -> "Call-In number must have 8 digits"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        callerLineId = it.callerLineId.copy(
                            number = e.number,
                            numberError = error
                        )
                    )
                }
            }

            is SetupDeviceEvent.CallerLineIdEvent.OnLocationChange -> {
                val error = when (val result = inputValidator.validateLocationInRange(
                    e.location,
                    uiState.value.callerLineId.locationRange
                )) {
                    is Result.Failure -> when (result.error) {
                        InputError.LocationError.EMPTY -> "Location cannot be empty"
                        InputError.LocationError.OUT_OF_RANGE -> "Location must be between ${uiState.value.callerLineId.formatedLocationRange()}"
                        InputError.LocationError.INVALID_FORMAT -> "Invalid location format"
                    }

                    is Result.Success -> null
                }
                _uiState.update {
                    it.copy(
                        callerLineId = it.callerLineId.copy(
                            location = e.location,
                            locationError = error
                        )
                    )
                }
            }

            SetupDeviceEvent.CallerLineIdEvent.OnFindLocation -> {
                executeIfValidSimNumber {
                    _uiState.update { it.copy(callerLineId = it.callerLineId.copy(isFetchingLocation = true)) }
                    viewModelScope.launch {
                        val location = requestNextLocation(Constants.FIND_CLI_LOCATION_REQUEST)
                        _uiState.update {
                            it.copy(
                                callerLineId = it.callerLineId.copy(
                                    location = location ?: "", isFetchingLocation = false
                                )
                            )
                        }
                    }
                }
            }

            SetupDeviceEvent.CallerLineIdEvent.OnUpdateMode -> {
                executeIfValidSimNumber {
                    _uiState.update { it.copy(callerLineId = it.callerLineId.copy(isUpdatingMode = true)) }
                    deviceRepository.setCliMode(
                        simNumber = uiState.value.simAndPasswordState.simNumber,
                        password = uiState.value.currentProgrammingPassword,
                        cliMode = uiState.value.callerLineId.userMode.name
                    )
                }
            }

            SetupDeviceEvent.CallerLineIdEvent.OnUpdateClick -> {
                executeIfValidSimNumber {
                    _uiState.update { it.copy(callerLineId = it.callerLineId.copy(isUpdatingNumber = true)) }
                    deviceRepository.setCliNumber(
                        simNumber = uiState.value.simAndPasswordState.simNumber,
                        password = uiState.value.currentProgrammingPassword,
                        location = uiState.value.callerLineId.location,
                        cliNumber = uiState.value.callerLineId.number
                    )
                }
            }
        }
    }
}