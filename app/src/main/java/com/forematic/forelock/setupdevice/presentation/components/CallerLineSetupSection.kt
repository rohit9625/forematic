package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.setupdevice.presentation.CallerLineIdentification
import com.forematic.forelock.setupdevice.presentation.SetupDeviceEvent
import com.forematic.forelock.setupdevice.presentation.UserMode
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun CallerLineSetupSection(
    callerLineId: CallerLineIdentification,
    onEvent: (SetupDeviceEvent.CallerLineIdEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = "Setup CLI(Caller-Line Identification)",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
        Card {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Choose user mode",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChipWithToolTip(
                            isSelected = callerLineId.userMode == UserMode.ANY,
                            onClick = {
                                onEvent(SetupDeviceEvent.CallerLineIdEvent.OnUserModeChange(UserMode.ANY))
                            },
                            label = "Any"
                        )

                        FilterChipWithToolTip(
                            isSelected = callerLineId.userMode == UserMode.AUTHORIZED,
                            onClick = {
                                onEvent(SetupDeviceEvent.CallerLineIdEvent.OnUserModeChange(UserMode.AUTHORIZED))
                            },
                            label = "Authorized"
                        )
                    }
                }

                CallerLineIdWithLocation(
                    number = callerLineId.number,
                    onNumberChange = {
                        onEvent(SetupDeviceEvent.CallerLineIdEvent.OnNumberChange(it))
                    },
                    location = callerLineId.location,
                    onLocationChange = {
                        onEvent(SetupDeviceEvent.CallerLineIdEvent.OnLocationChange(it))
                    },
                    onFindAction = {
                        onEvent(SetupDeviceEvent.CallerLineIdEvent.OnFindLocation)
                    },
                    label = "Setup Authorized Call-In Number",
                    locationRange = callerLineId.locationRange,
                    isEnabled = callerLineId.userMode == UserMode.AUTHORIZED
                )

                HorizontalDivider()

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Enter the last 8 digits of the caller's number",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.widthIn(max = 200.dp)
                    )

                    Button(
                        onClick = {
                            onEvent(SetupDeviceEvent.CallerLineIdEvent.OnUpdateClick)
                        },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(text = "Update")
                    }
                }
            }
        }
    }
}

@Composable
fun CallerLineIdWithLocation(
    number: String,
    onNumberChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    onFindAction: () -> Unit,
    label: String,
    locationRange: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            LabeledTextField(
                value = location,
                onValueChange = onLocationChange,
                label = "Location",
                secondaryLabel = locationRange,
                modifier = Modifier.widthIn(max = 148.dp),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    Button(
                        onClick = onFindAction,
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                        contentPadding = PaddingValues(),
                        enabled = isEnabled,
                        modifier = Modifier.height(IntrinsicSize.Max)
                    ) {
                        Text(
                            text = "Find\nNext",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                isEnabled = isEnabled,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                )
            )

            LabeledTextField(
                value = number,
                onValueChange = onNumberChange,
                label = "Caller Number",
                modifier = Modifier.widthIn(max = 172.dp),
                shape = RoundedCornerShape(12.dp),
                isEnabled = isEnabled,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                )
            )
        }
    }
}

@Preview
@Composable
private fun CallerLineSetupSectionPreview() {
    ForeLockTheme {
        Surface {
            CallerLineSetupSection(
                callerLineId = CallerLineIdentification(),
                onEvent = { },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}