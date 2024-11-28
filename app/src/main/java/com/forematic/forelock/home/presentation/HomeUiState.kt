package com.forematic.forelock.home.presentation

data class HomeUiState(
    val pinNumber: String = "",
    val targetGsmNumber: String = "",
    val errorMessage: String? = null,
    val showOpenGateDialog: Boolean = false
)