package com.forematic.forelock.setupdevice.presentation

sealed interface SetupDeviceEvent {
    data class DeviceTypeChanged(val deviceType: DeviceType): SetupDeviceEvent

    sealed interface SimAndPasswordEvent: SetupDeviceEvent {
        data class OnSimNumberChange(val number: String): SimAndPasswordEvent
        data class OnPasswordChange(val password: String): SimAndPasswordEvent
        data object OnUpdateClick: SimAndPasswordEvent
    }

    sealed interface OutputRelayEvent: SetupDeviceEvent {
        data class OnRelay1NameChange(val name: String): OutputRelayEvent
        data class OnRelay1TextChange(val text: OutputRelayText): OutputRelayEvent
        data class OnRelay1TimeChange(val relayTime: String): OutputRelayEvent
        data class OnRelay1IconChange(val icon: Int): OutputRelayEvent
        data object OnGetNameForRelay1: OutputRelayEvent

        data class OnRelay2NameChange(val name: String): OutputRelayEvent
        data class OnRelay2TextChange(val text: OutputRelayText): OutputRelayEvent
        data class OnRelay2TimeChange(val relayTime: String): OutputRelayEvent
        data class OnRelay2IconChange(val icon: Int): OutputRelayEvent
        data object OnGetNameForRelay2: OutputRelayEvent

        data object OnUpdateClick: OutputRelayEvent
    }

    sealed interface TimezoneModeEvent: SetupDeviceEvent {
        data class OnTimezoneModeChange(val timezoneMode: TimezoneMode): TimezoneModeEvent
        data object OnUpdateClick: TimezoneModeEvent
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
        data class OnUserModeChange(val userMode: CallerLineMode): CallerLineIdEvent
        data class OnNumberChange(val number: String): CallerLineIdEvent
        data class OnLocationChange(val location: String): CallerLineIdEvent
        data object OnFindLocation: CallerLineIdEvent
        data object OnUpdateMode: CallerLineIdEvent
        data object OnUpdateClick: CallerLineIdEvent
    }

    sealed interface CallOutNumberEvent: SetupDeviceEvent {
        data class OnFirstNumberChange(val number: String): CallOutNumberEvent
        data class OnFirstNameChange(val name: String): CallOutNumberEvent

        data class OnSecondNumberChange(val number: String): CallOutNumberEvent
        data class OnSecondNameChange(val name: String): CallOutNumberEvent
        data class OnThirdNumberChange(val number: String): CallOutNumberEvent
        data class OnThirdNameChange(val name: String): CallOutNumberEvent

        data class OnAdminNumberChange(val number: String): CallOutNumberEvent
        data object OnChangeClick: CallOutNumberEvent
        data object OnUpdateClick: CallOutNumberEvent
    }

    sealed interface VolumeSettingsEvent: SetupDeviceEvent {
        data class OnSpeakerVolumeChange(val volume: Float): VolumeSettingsEvent
        data class OnMicVolumeChange(val volume: Float): VolumeSettingsEvent
        data object OnSaveSpeakerVolume: VolumeSettingsEvent
        data object OnSaveMicVolume: VolumeSettingsEvent
    }

    data object OnCheckSignalStrength: SetupDeviceEvent
}