package com.forematic.forelock.setupdevice.presentation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AssistChip
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forematic.forelock.setupdevice.presentation.components.CustomDropDown
import com.forematic.forelock.setupdevice.presentation.components.LabeledBox
import com.forematic.forelock.setupdevice.presentation.components.LabeledTextField
import com.forematic.forelock.setupdevice.presentation.components.ToolTipWithIcon
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
    var showCallOutNumberInfo by remember { mutableStateOf(false) }


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
                                onValueChange = { onEvent(
                                    SetupDeviceEvent.ProgrammingPasswordChanged(
                                        it
                                    )
                                ) },
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
                                    },
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

            CallOutNumbers(
                callOutNumbers = uiState.callOutNumbers,
                onEvent = { event -> onEvent(event) },
                modifier = Modifier.padding(8.dp),
                canAddMoreNumbers = uiState.canAddMoreCallOutNumbers,
                showToolTip = showCallOutNumberInfo,
                onToolTipClick = { showCallOutNumberInfo = !showCallOutNumberInfo },
                onToolTipDismiss = { showCallOutNumberInfo = false }
            )

            AudioAdjustments(
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun OutputRelaySection1(
    outputName: String,
    onOutputNameChange: (String)-> Unit,
    modifier: Modifier = Modifier,
    label: String = "Output Relay",
    showToolTip: Boolean = false,
    onToolTipClick: () -> Unit = { },
    onToolTipDismiss: () -> Unit = { }
) {
    LabeledBox(
        label = label,
        modifier = modifier,
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutputNaming(
                    outputName = outputName,
                    onOutputNameChange = onOutputNameChange
                )
            }
        }
    )
}

@Composable
private fun OutputNaming(
    outputName: String,
    onOutputNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            LabeledTextField(
                label = "Output Name",
                value = "",
                onValueChange = {
//                            onEvent(SetupDeviceEvent.OutputRelayEvent.UpdateNameRelay1(it))
                },
                trailingIcon = {
                    ToolTipWithIcon(
                        showToolTip = false,
                        onClick = { },
                        onDismiss = { },
                        infoText = "This is a sample text."
                    )
                },
                modifier = Modifier.weight(0.7f),
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            )

            Row {
                Button(
                    onClick = { /*TODO("Fetch output name from the device")*/ },
                    modifier = Modifier.widthIn(max = 96.dp),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Get Output Name",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                    )
                }

                ToolTipWithIcon(
                    showToolTip = false,
                    onClick = { },
                    onDismiss = { },
                    infoText = "This is a sample text."
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
            ) {
                Text(
                    text = "Choose icon text",
                    style = MaterialTheme.typography.labelMedium
                )
                CustomDropDown(
                    isExpanded = false,
                    onExpandedChange = { },
                    value = "Open/Close",
                    onValueChange = {
//                                onEvent(SetupDeviceEvent.OutputRelayEvent.UpdateTextRelay1(it))
                    },
                    options = listOf("Open/Close", "Lock/Unlock", "Up/Down", "On/Off", "Set/Unset"),
                    modifier = Modifier.width(156.dp)
                )
            }

            AssistChip(
                onClick = { /*TODO("Open select icon dialog")*/ },
                label = {
                    Text(
                        text = "Choose an icon",
                        modifier = Modifier.padding(vertical = 4.dp),
                        textAlign = TextAlign.Center,
                    )
                },
                modifier = Modifier.widthIn(max = 96.dp)
            )
        }
    }
}

@Preview
@Composable
private fun OutputRelaySectionPreview() {
    ForeLockTheme {
        Surface {
            Column {
                OutputRelaySection1(
                    outputName = "Jaguar",
                    onOutputNameChange = { },
                    label = "Output Naming 1",
                    modifier = Modifier.padding(8.dp).fillMaxWidth()
                )

                OutputRelaySection1(
                    outputName = "Jaguar",
                    onOutputNameChange = { },
                    label = "Output Naming 2",
                    modifier = Modifier.padding(8.dp).fillMaxWidth()
                )
            }
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

@Composable
fun CallOutNumbers(
    callOutNumbers: List<CallOutNumber>,
    onEvent: (SetupDeviceEvent.CallOutNumberEvent) -> Unit,
    modifier: Modifier = Modifier,
    canAddMoreNumbers: Boolean = true,
    showToolTip: Boolean = false,
    onToolTipClick: () -> Unit = { },
    onToolTipDismiss: () -> Unit = { }
) {
    LabeledBox(
        label = "Call-out Numbers",
        modifier = modifier,
        content = {
            Box {
                Column(
                    modifier = Modifier
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    callOutNumbers.forEachIndexed { index, item->
                        Row(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${index + 1}.")
                            LabeledTextField(
                                label = "Call Number",
                                value = item.number,
                                onValueChange = {
                                    onEvent(
                                        SetupDeviceEvent.CallOutNumberEvent.UpdateNumber(
                                            index,
                                            it
                                        )
                                    )
                                },
                                modifier = Modifier.weight(0.6f),
                                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                            )

                            LabeledTextField(
                                label = "Name",
                                value = item.name,
                                onValueChange = {
                                    onEvent(
                                        SetupDeviceEvent.CallOutNumberEvent.UpdateName(
                                            index,
                                            it
                                        )
                                    )
                                },
                                modifier = Modifier.weight(0.4f),
                                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                            )
                        }
                    }

                    AssistChip(
                        onClick = { onEvent(SetupDeviceEvent.CallOutNumberEvent.AddMoreNumber) },
                        label = { Text(text = "More") },
                        enabled = canAddMoreNumbers,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.align(Alignment.End)
                    )
                }
                ToolTipWithIcon(
                    showToolTip = showToolTip,
                    onClick = onToolTipClick,
                    onDismiss = onToolTipDismiss,
                    infoText = "If a call is not answered, it redirects call to the next number the list.",
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    )
}

//@Preview
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