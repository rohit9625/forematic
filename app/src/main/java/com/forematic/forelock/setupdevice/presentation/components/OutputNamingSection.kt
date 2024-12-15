package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.setupdevice.presentation.OutputRelay
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
                    onOutputNameChange = { },
                    onGetOutputName = { },
                    label = "Output Naming 1",
                    modifier = Modifier.fillMaxWidth()
                )

                outputRelay2?.let { relay ->
                    HorizontalDivider()

                    OutputNaming(
                        outputName = relay.name,
                        onOutputNameChange = { },
                        onGetOutputName = { },
                        label = "Output Naming 2",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun OutputNaming(
    outputName: String,
    onOutputNameChange: (String) -> Unit,
    onGetOutputName: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Output Relay"
) {
    var isNameToolTipVisible by remember { mutableStateOf(false) }
    var isButtonToolTipVisible by remember { mutableStateOf(false) }

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
                    value = "",
                    onValueChange = { },
                    trailingIcon = {
                        ToolTipWithIcon(
                            showToolTip = isNameToolTipVisible,
                            onClick = { isNameToolTipVisible = true },
                            onDismiss = { isNameToolTipVisible = false },
                            infoText = "This is a sample text."
                        )
                    },
                    modifier = Modifier.weight(0.7f)
                )

                Row {
                    Button(
                        onClick = { /*TODO("Fetch output name from the device")*/ },
                        modifier = Modifier.widthIn(max = 96.dp),
                        shape = RoundedCornerShape(12.dp),
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
                        onValueChange = { },
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