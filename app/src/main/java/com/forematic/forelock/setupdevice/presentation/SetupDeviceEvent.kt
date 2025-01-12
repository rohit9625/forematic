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
        data class OnRelay1NameChange(val name: String): OutputRelayEvent
        data class OnRelay1TextChange(val text: OutputRelayText): OutputRelayEvent
        data class OnRelay1TimeChange(val relayTime: String): OutputRelayEvent
        data object OnGetNameForRelay1: OutputRelayEvent

        data class OnRelay2NameChange(val name: String): OutputRelayEvent
        data class OnRelay2TextChange(val text: OutputRelayText): OutputRelayEvent
        data class OnRelay2TimeChange(val relayTime: String): OutputRelayEvent
        data object OnGetNameForRelay2: OutputRelayEvent

        data object OnUpdateClick: OutputRelayEvent
    }

    sealed interface CallOutNumberEvent: SetupDeviceEvent {
        data class UpdateNumber(val index: Int, val number: String): CallOutNumberEvent
        data class UpdateName(val index: Int, val name: String): CallOutNumberEvent
        data object AddMoreNumber: CallOutNumberEvent
    }
}