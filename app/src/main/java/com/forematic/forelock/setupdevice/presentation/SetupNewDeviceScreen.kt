package com.forematic.forelock.setupdevice.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forematic.forelock.setupdevice.presentation.components.AudioAdjustmentSection
import com.forematic.forelock.setupdevice.presentation.components.CallInNumberSection
import com.forematic.forelock.setupdevice.presentation.components.CallOutNumberSection
import com.forematic.forelock.setupdevice.presentation.components.OutputNamingSection
import com.forematic.forelock.setupdevice.presentation.components.SetKeypadCodeSection
import com.forematic.forelock.setupdevice.presentation.components.SimNumberAndPasswordSection
import com.forematic.forelock.setupdevice.presentation.components.TimezoneModeSection
import com.forematic.forelock.ui.theme.ForeLockTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupNewDeviceScreen(
    uiState: NewDeviceUiState,
    onEvent: (SetupDeviceEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Setup New Device",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            SimNumberAndPasswordSection(
                simNumber = uiState.simAndPasswordState.simNumber,
                onSimNumberChange = {
                    onEvent(SetupDeviceEvent.SimAndPasswordEvent.OnSimNumberChange(it))
                },
                password = uiState.simAndPasswordState.programmingPassword,
                onPasswordChange = {
                    onEvent(SetupDeviceEvent.SimAndPasswordEvent.OnPasswordChange(it))
                },
                onUpdateClick = {
                    onEvent(SetupDeviceEvent.SimAndPasswordEvent.OnUpdateClick)
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            OutputNamingSection(
                outputRelay1 = uiState.outputRelay1,
                outputRelay2 = uiState.outputRelay2,
                onEvent = onEvent,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            TimezoneModeSection(
                selectedMode = uiState.timezoneMode,
                onModeSelection = { onEvent(SetupDeviceEvent.TimezoneModeChanged(it)) },
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            SetKeypadCodeSection(
                keypadCode1 = uiState.keypadCode1,
                keypadCode2 = uiState.keypadCode2,
                deliveryCode = uiState.deliveryCode,
                onEvent = onEvent,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            CallInNumberSection(
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            CallOutNumberSection(
                callOutNumbers = uiState.callOutNumbers,
                onEvent = { event -> onEvent(event) },
                modifier = Modifier.padding(horizontal = 8.dp),
                canAddMoreNumbers = uiState.canAddMoreCallOutNumbers
            )

            AudioAdjustmentSection(
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun SetupNewDeviceScreenPreview() {
    ForeLockTheme {
        SetupNewDeviceScreen(
            uiState = NewDeviceUiState(),
            onEvent = { },
            onNavigateBack = { }
        )
    }
}