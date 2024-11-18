package com.forematic.forelock.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forematic.forelock.ui.components.CustomDropDown
import com.forematic.forelock.ui.components.LabeledBox
import com.forematic.forelock.ui.components.LabeledTextField
import com.forematic.forelock.ui.components.ToolTipWithIcon
import com.forematic.forelock.ui.theme.ForeLockTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SetupNewDeviceScreen(
    uiState: NewDeviceUiState,
    onEvent: (SetupDeviceEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    var isDeviceTypeExpanded by remember { mutableStateOf(false) }
    var isRelayTex1Expanded by remember { mutableStateOf(false) }
    var isOutputNamingExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var isToolTipVisible by remember { mutableStateOf(false) }
    var modeToolTip : TimezoneMode? by remember { mutableStateOf(null) }

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
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            LabeledBox(
                label = "Device Type",
                modifier = Modifier.padding(8.dp),
                content = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Choose a device type",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        CustomDropDown(
                            isExpanded = isDeviceTypeExpanded,
                            onExpandedChange = { isDeviceTypeExpanded = it },
                            value = uiState.deviceType,
                            onValueChange = {
                                onEvent(SetupDeviceEvent.DeviceTypeChanged(it))
                                isDeviceTypeExpanded = false
                            },
                            options = DeviceType.entries,
                            modifier = Modifier.width(164.dp)
                        )
                    }
                }
            )

            LabeledBox(
                label = "SIM Card",
                modifier = Modifier.padding(8.dp),
                content = {
                    Column(
                        modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        LabeledTextField(
                            label = "Enter SIM card number",
                            value = uiState.simNumber,
                            onValueChange = { onEvent(SetupDeviceEvent.SimNumberChanged(it)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            LabeledTextField(
                                label = "Programming password",
                                value = uiState.programmingPassword,
                                onValueChange = { onEvent(SetupDeviceEvent.ProgrammingPasswordChanged(it)) },
                                modifier = Modifier.width(164.dp),
                                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.align(Alignment.Bottom)
                            ) {
                                Button(
                                    onClick = { },
                                    modifier = Modifier,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(text = "Get ID")
                                }
                                ToolTipWithIcon(
                                    showToolTip = isToolTipVisible,
                                    onClick = { isToolTipVisible = true },
                                    onDismiss = { isToolTipVisible = false },
                                    infoText = "Read from device"
                                )
                            }
                        }
                    }
                }
            )

            LabeledBox(
                label = "Output Relay",
                modifier = Modifier.padding(8.dp),
                content = {
                    Column(
                        modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            LabeledTextField(
                                label = "Relay Name",
                                value = uiState.outputRelay1.name,
                                onValueChange = {
                                    onEvent(SetupDeviceEvent.OutputRelayEvent.UpdateNameRelay1(it))
                                },
                                modifier = Modifier.weight(0.7f),
                                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                            )

                            LabeledTextField(
                                label = "Relay Time",
                                value = uiState.outputRelay1.relayTime,
                                onValueChange = {
                                    onEvent(SetupDeviceEvent.OutputRelayEvent.UpdateTimeRelay1(it))
                                },
                                modifier = Modifier.weight(0.3f),
                                placeholder = {
                                    Text("0-99")
                                },
                                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                                textStyle = MaterialTheme.typography.bodySmall,
                                trailingIcon = {
                                    Text(text = "sec.")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.NumberPassword
                                )
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                        ) {
                            Text(
                                text = "Text",
                                style = MaterialTheme.typography.labelMedium
                            )
                            CustomDropDown(
                                isExpanded = isRelayTex1Expanded,
                                onExpandedChange = { isRelayTex1Expanded = it },
                                value = uiState.outputRelay1.text,
                                onValueChange = {
                                    onEvent(SetupDeviceEvent.OutputRelayEvent.UpdateTextRelay1(it))
                                    isRelayTex1Expanded = false
                                },
                                options = OutputRelayText.entries,
                                modifier = Modifier.width(156.dp)
                            )
                        }
                    }
                }
            )

            LabeledBox(
                label = "Timezone Mode",
                modifier = Modifier.padding(8.dp),
                content = {
                    FlowRow(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TimezoneMode.entries.forEach { mode ->
                            Row(
                                modifier = Modifier.width(IntrinsicSize.Max)
                            ) {
                                FilterChip(
                                    selected = mode == uiState.timezoneMode,
                                    onClick = { onEvent(SetupDeviceEvent.TimezoneModeChanged(mode)) },
                                    label = { Text(text = mode.displayName) },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(mode.icon),
                                            contentDescription = null
                                        )
                                    }
                                )

                                ToolTipWithIcon(
                                    showToolTip = modeToolTip == mode,
                                    onClick = { modeToolTip = mode },
                                    onDismiss = { modeToolTip = null },
                                    infoText = "This is a sample text."
                                )
                            }
                        }
                    }
                }
            )

            AudioAdjustments(
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun AudioAdjustments(modifier: Modifier = Modifier) {
    LabeledBox(
        label = "Audio Adjustments",
        modifier = modifier,
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Panel Speaker Volume",
                        style = MaterialTheme.typography.labelMedium
                    )
                    SliderWithCustomTrackAndThumb()
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Panel Microphone Volume",
                        style = MaterialTheme.typography.labelMedium
                    )
                    SliderWithCustomTrackAndThumb()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SliderWithCustomTrackAndThumb() {
    val sliderState = remember {
        SliderState(
            valueRange = 0f..8f,
            onValueChangeFinished = {
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)
            },
            steps = 7
        )
    }
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Slider(
            state = sliderState,
            modifier = Modifier
                .weight(0.9f)
                .semantics { contentDescription = "Localized Description" },
            interactionSource = interactionSource,
            thumb = {
                SliderDefaults.Thumb(interactionSource = interactionSource)
            },
            track = { SliderDefaults.Track(sliderState = sliderState) }
        )

        Text(
            text = "%.0f".format(sliderState.value),
            modifier = Modifier.weight(0.1f),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}


@Preview
@Composable
private fun AudioAdjustmentPreview() {
    ForeLockTheme {
        Surface {
            AudioAdjustments(modifier = Modifier.padding(16.dp))
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