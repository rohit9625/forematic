package com.forematic.forelock.setupdevice.presentation

sealed interface SetupDeviceEvent {
    data class DeviceTypeChanged(val deviceType: DeviceType): SetupDeviceEvent
    data class TimezoneModeChanged(val timezoneMode: TimezoneMode): SetupDeviceEvent

    sealed interface SimAndPasswordEvent: SetupDeviceEvent {
        data class OnSimNumberChange(val number: String): SimAndPasswordEvent
        data class OnPasswordChange(val password: String): SimAndPasswordEvent
        data object OnUpdateClick: SimAndPasswordEvent
    }

    sealed interface OutputRelayEvent: SetupDeviceEvent {
        data class UpdateNameRelay1(val name: String): OutputRelayEvent
        data class UpdateTextRelay1(val text: OutputRelayText): OutputRelayEvent
        data class UpdateTimeRelay1(val relayTime: String): OutputRelayEvent

        data class UpdateNameRelay2(val name: String): OutputRelayEvent
        data class UpdateTextRelay2(val text: OutputRelayText): OutputRelayEvent
        data class UpdateTimeRelay2(val relayTime: String): OutputRelayEvent
    }

    sealed interface CallOutNumberEvent: SetupDeviceEvent {
        data class UpdateNumber(val index: Int, val number: String): CallOutNumberEvent
        data class UpdateName(val index: Int, val name: String): CallOutNumberEvent
        data object AddMoreNumber: CallOutNumberEvent
    }
}