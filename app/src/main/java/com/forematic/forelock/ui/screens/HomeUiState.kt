package com.forematic.forelock.ui.screens

data class HomeUiState(
    val pinNumber: String = "",
    val targetGsmNumber: String = "",
    val errorMessage: String? = null,
    val showOpenGateDialog: Boolean = false
)