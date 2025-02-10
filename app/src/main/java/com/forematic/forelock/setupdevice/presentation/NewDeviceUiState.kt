package com.forematic.forelock.setupdevice.presentation

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import com.forematic.forelock.R

data class NewDeviceUiState(
    val deviceType: DeviceType = DeviceType.G24_INTERCOM,
    val currentProgrammingPassword: String = "",
    val simAndPasswordState: SimAndPasswordState = SimAndPasswordState(programmingPassword = currentProgrammingPassword),
    val outputRelay1: OutputRelay = OutputRelay(),
    val outputRelay2: OutputRelay? = null,
    val timezoneMode: TimezoneMode = TimezoneMode.FREE,
    val currentTimezoneMode: TimezoneMode = TimezoneMode.FREE,
    val timezoneError: String? = null,
    val isUpdatingTimezone: Boolean = false,
    val keypadCode1: KeypadCodeForOutput = KeypadCodeForOutput(locationRange = 1..100),
    val keypadCode2: KeypadCodeForOutput = KeypadCodeForOutput(locationRange = 101..150),
    val deliveryCode: KeypadCodeForOutput = KeypadCodeForOutput(locationRange = 151..200),
    val isUpdatingKeypadCodes: Boolean = false,
    val callerLineId: CallerLineIdentification = CallerLineIdentification(locationRange = 201..250),
    val firstCallOut: CallOutNumberUiState = CallOutNumberUiState(),
    val secondCallOut: CallOutNumberUiState = CallOutNumberUiState(),
    val thirdCallOut: CallOutNumberUiState = CallOutNumberUiState(),
    val isUpdatingCallOutNumbers: Boolean = false,
    val adminNumber: String = "",
    val adminNumberError: String? = null,
    val isUpdatingAdminNumber: Boolean = false,
    val speakerVolume: Float = 0f,
    val micVolume: Float = 0f,
    val signalStrength: Int? = null,
    val isRefreshingSignal: Boolean = false
)

data class CallOutNumberUiState(
    val number: String = "",
    val name: String = "",
    val numberError: String? = null,
    val nameError: String? = null
)

data class SimAndPasswordState(
    val simNumber: String = "",
    val programmingPassword: String = "",
    val isLoading: Boolean = false,
    val simNumberError: String? = null,
    val passwordError: String? = null
)

data class OutputRelay(
    val name: String = "",
    val text: OutputRelayText = OutputRelayText.OPEN_CLOSE,
    val relayTime: String = "",
    val relayTimeRange: IntRange = 0..99,
    @DrawableRes val icon: Int = R.drawable.ic_rounded_image_24,
    val isFetchingOutputName: Boolean = false,
    val relayTimeError: String? = null,
    val outputNameError: String? = null,
    val otherError: String? = null
) {
    fun isRelayTimeInRange(): Boolean {
        val time = relayTime.toIntOrNull() ?: return false // Handle parsing errors
        return time in relayTimeRange
    }
    fun isAnyInputEmpty(): Boolean {
        return name.isBlank() || relayTime.isBlank() || icon == R.drawable.ic_rounded_image_24
    }
}

data class KeypadCodeForOutput(
    val code: String = "",
    val location: String = "",
    val locationRange: IntRange = 0..0,
    val isFetchingLocation: Boolean = false,
    val codeError: String? = null,
    val locationError: String? = null
) {
    @SuppressLint("DefaultLocale")
    fun formatedLocationRange(): String {
        val start = locationRange.first
        val end = locationRange.last
        val formattedStart = String.format("%03d", start)
        val formattedEnd = String.format("%03d", end)
        return "$formattedStart-$formattedEnd"
    }

    fun hasErrors(): Boolean {
        return code.isBlank() || location.isBlank() || locationError != null || codeError != null
    }
}

data class CallerLineIdentification(
    val userMode: CallerLineMode = CallerLineMode.ANY,
    val currentUserMode: CallerLineMode = CallerLineMode.ANY,
    val number: String = "",
    val location: String = "",
    val locationRange: IntRange = 201..250,
    val locationError: String? = null,
    val isFetchingLocation: Boolean = false,
    val isUpdatingMode: Boolean = false,
    val isUpdatingNumber: Boolean = false,
    val numberError: String? = null
) {
    @SuppressLint("DefaultLocale")
    fun formatedLocationRange(): String {
        val start = locationRange.first
        val end = locationRange.last
        val formattedStart = String.format("%03d", start)
        val formattedEnd = String.format("%03d", end)
        return "$formattedStart-$formattedEnd"
    }
}

enum class CallerLineMode(val displayName: String) {
    ANY("Any"),
    AUTHORIZED("Authorized")
}

enum class OutputRelayText(val displayName: String) {
    OPEN_CLOSE("Open/Close"),
    UNLOCK_LOCK("Unlock/Lock"),
    UP_DOWN("Up/Down"),
    ON_OFF("On/Off"),
    SET_UNSET("Set/Unset")
}

enum class TimezoneMode(val displayName: String, val code: String, @DrawableRes val icon: Int) {
    FREE("Trade", "FREE", R.drawable.ic_trade_24),
    DAY("Day", "DAY", R.drawable.ic_rounded_day_24),
    NIGHT("Night", "NIGHT", R.drawable.ic_outline_night_24),
    HOLIDAY("Holiday", "HOLS", R.drawable.ic_block_24)
}

enum class DeviceType(val displayName: String) {
    G24_INTERCOM("G24 Intercom"),
    G64_INTERCOM("G64 Intercom"),
    R20_RELAY("R20 Relay")
}
