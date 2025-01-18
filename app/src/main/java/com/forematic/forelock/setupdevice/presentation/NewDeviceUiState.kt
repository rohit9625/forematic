package com.forematic.forelock.setupdevice.presentation

import androidx.annotation.DrawableRes
import com.forematic.forelock.R

data class NewDeviceUiState(
    val deviceType: DeviceType = DeviceType.G24_INTERCOM,
    val simAndPasswordState: SimAndPasswordState = SimAndPasswordState(),
    val outputRelay1: OutputRelay = OutputRelay(),
    val outputRelay2: OutputRelay? = null,
    val timezoneMode: TimezoneMode = TimezoneMode.FREE,
    val callOutNumbers: List<CallOutNumber> = listOf(CallOutNumber())
) {
    val canAddMoreCallOutNumbers: Boolean
        get() = callOutNumbers.size < 4
}

data class SimAndPasswordState(
    val simNumber: String = "",
    val programmingPassword: String = "",
    val error: String? = null
)

data class CallOutNumber(
    val number: String = "",
    val name: String = ""
)

data class OutputRelay(
    val name: String = "",
    val text: OutputRelayText = OutputRelayText.OPEN_CLOSE,
    val relayTime: String = "",
    @DrawableRes val icon: Int = R.drawable.ic_rounded_image_24,
    val error: String? = null
)

enum class OutputRelayText(val displayName: String) {
    OPEN_CLOSE("Open/Close"),
    UNLOCK_LOCK("Unlock/Lock"),
    UP_DOWN("Up/Down"),
    ON_OFF("On/Off"),
    SET_UNSET("Set/Unset")
}

enum class TimezoneMode(val displayName: String, @DrawableRes val icon: Int) {
    FREE("Free", R.drawable.ic_outline_free_24),
    DAY("Day", R.drawable.ic_rounded_day_24),
    NIGHT("Night", R.drawable.ic_outline_night_24),
//    NOTIFY("Notify", R.drawable.ic_outline_notifications_24)
}

enum class DeviceType(val displayName: String) {
    G24_INTERCOM("G24 Intercom"),
    G64_INTERCOM("G64 Intercom"),
    R20_RELAY("R20 Relay")
}
