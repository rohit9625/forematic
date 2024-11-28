package com.forematic.forelock.home.presentation

sealed interface HomeScreenEvent {
    data class UpdateDialogVisibility(val value: Boolean): HomeScreenEvent
    data class OnPinChange(val pin: String): HomeScreenEvent
    data class OnTargetNumberChange(val number: String): HomeScreenEvent
    data class OnSendSignal(val showSnack: ()-> Unit): HomeScreenEvent
}