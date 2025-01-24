package com.forematic.forelock.setupdevice.presentation

sealed interface SetupDeviceEvent {
    data class DeviceTypeChanged(val deviceType: DeviceType): SetupDeviceEvent
    data class TimezoneModeChanged(val timezoneMode: TimezoneMode): SetupDeviceEvent
    data object OnTimezoneModeUpdate: SetupDeviceEvent

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

    sealed interface KeypadCodeEvent: SetupDeviceEvent {
        data class OnKeypadCode1Change(val code: String): KeypadCodeEvent
        data class OnCodeLocation1Change(val location: String): KeypadCodeEvent
        data object OnFindKeypadCode1Location: KeypadCodeEvent

        data class OnKeypadCode2Change(val code: String): KeypadCodeEvent
        data class OnCodeLocation2Change(val location: String): KeypadCodeEvent
        data object OnFindKeypadCode2Location: KeypadCodeEvent

        data class OnDeliveryCodeChange(val code: String): KeypadCodeEvent
        data class OnDeliveryCodeLocationChange(val location: String): KeypadCodeEvent
        data object OnFindDeliveryCodeLocation: KeypadCodeEvent

        data object OnUpdateClick: KeypadCodeEvent
    }

    sealed interface CallerLineIdEvent: SetupDeviceEvent {
        data class OnUserModeChange(val userMode: UserMode): CallerLineIdEvent
        data class OnNumberChange(val number: String): CallerLineIdEvent
        data class OnLocationChange(val location: String): CallerLineIdEvent
        data object OnFindLocation: CallerLineIdEvent
    }

    sealed interface CallOutNumberEvent: SetupDeviceEvent {
        data class UpdateNumber(val index: Int, val number: String): CallOutNumberEvent
        data class UpdateName(val index: Int, val name: String): CallOutNumberEvent
        data object AddMoreNumber: CallOutNumberEvent
    }
}