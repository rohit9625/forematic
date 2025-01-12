package com.forematic.forelock.setupdevice.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.setupdevice.presentation.OutputRelay
import com.forematic.forelock.setupdevice.presentation.OutputRelayText
import com.forematic.forelock.setupdevice.presentation.SetupDeviceEvent
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun OutputNamingSection(
    outputRelay1: OutputRelay,
    onEvent: (SetupDeviceEvent.OutputRelayEvent) -> Unit,
    modifier: Modifier = Modifier,
    outputRelay2: OutputRelay? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Output Relay Naming",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
        Card {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                OutputNaming(
                    outputName = outputRelay1.name,
                    relayTime = outputRelay1.relayTime,
                    outputRelayText = outputRelay1.text,
                    icon = outputRelay1.icon,
                    onOutputNameChange = {
                        onEvent(SetupDeviceEvent.OutputRelayEvent.OnRelay1NameChange(it))
                    },
                    onRelayTimeChange = {
                        onEvent(SetupDeviceEvent.OutputRelayEvent.OnRelay1TimeChange(it))
                    },
                    onRelayTextChange = {
                        onEvent(SetupDeviceEvent.OutputRelayEvent.OnRelay1TextChange(it))
                    },
                    onGetOutputName = {
                        onEvent(SetupDeviceEvent.OutputRelayEvent.OnGetNameForRelay1)
                    },
                    label = "Output Naming 1",
                    modifier = Modifier.fillMaxWidth()
                )

                outputRelay2?.let { relay ->
                    HorizontalDivider()

                    OutputNaming(
                        outputName = relay.name,
                        relayTime = outputRelay2.relayTime,
                        outputRelayText = outputRelay2.text,
                        icon = outputRelay2.icon,
                        onOutputNameChange = {
                            onEvent(SetupDeviceEvent.OutputRelayEvent.OnRelay2NameChange(it))
                        },
                        onRelayTimeChange = {
                            onEvent(SetupDeviceEvent.OutputRelayEvent.OnRelay2TimeChange(it))
                        },
                        onRelayTextChange = {
                            onEvent(SetupDeviceEvent.OutputRelayEvent.OnRelay2TextChange(it))
                        },
                        onGetOutputName = {
                            onEvent(SetupDeviceEvent.OutputRelayEvent.OnGetNameForRelay2)
                        },
                        label = "Output Naming 2",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                HorizontalDivider()

                Button(
                    onClick = { onEvent(SetupDeviceEvent.OutputRelayEvent.OnUpdateClick) },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Update")
                }
            }
        }
    }
}

@Composable
private fun OutputNaming(
    outputName: String,
    relayTime: String,
    outputRelayText: OutputRelayText,
    @DrawableRes icon: Int,
    onOutputNameChange: (String) -> Unit,
    onRelayTimeChange: (String) -> Unit,
    onRelayTextChange: (OutputRelayText) -> Unit,
    onGetOutputName: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Output Relay"
) {
    var isNameToolTipVisible by remember { mutableStateOf(false) }
    var isButtonToolTipVisible by remember { mutableStateOf(false) }
    var isIconTextDropDownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                LabeledTextField(
                    label = "Output Name",
                    value = outputName,
                    onValueChange = onOutputNameChange,
                    placeholder = "Eg. Office Gate",
                    trailingIcon = {
                        ToolTipWithIcon(
                            showToolTip = isNameToolTipVisible,
                            onClick = { isNameToolTipVisible = true },
                            onDismiss = { isNameToolTipVisible = false },
                            infoText = "This is a sample text."
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier.weight(0.7f)
                )

                Row {
                    Button(
                        onClick = onGetOutputName,
                        modifier = Modifier.widthIn(max = 96.dp),
                        shape = MaterialTheme.shapes.medium,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Get Output Name",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    ToolTipWithIcon(
                        showToolTip = isButtonToolTipVisible,
                        onClick = { isButtonToolTipVisible = true },
                        onDismiss = { isButtonToolTipVisible = false },
                        infoText = "This is a sample text."
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                ) {
                    Text(
                        text = "Choose Icon Text",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    CustomDropDown(
                        isExpanded = isIconTextDropDownExpanded,
                        onExpandedChange = { isIconTextDropDownExpanded = it },
                        value = outputRelayText,
                        onValueChange = {
                            onRelayTextChange(it)
                            isIconTextDropDownExpanded = false
                        },
                        options = OutputRelayText.entries,
                        modifier = Modifier.width(156.dp)
                    )
                }

                LabeledTextField(
                    label = "Relay Time",
                    value = relayTime,
                    onValueChange = onRelayTimeChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    placeholder = "Eg. 25",
                    modifier = Modifier.widthIn(max = 96.dp).weight(1f, false)
                )

                Column {
                    Text(
                        text = "Icon",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    OutlinedCard(
                        onClick = { },
                        modifier = Modifier,
                        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.tertiary),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Image(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp).size(36.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun OutputNamingSectionPreview() {
    ForeLockTheme {
        Surface {
            OutputNamingSection(
                outputRelay1 = OutputRelay(),
                outputRelay2 = OutputRelay(),
                onEvent = { },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}