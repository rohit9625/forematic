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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.home.presentation.components.IconButtonWithLoader
import com.forematic.forelock.setupdevice.presentation.CallerLineIdentification
import com.forematic.forelock.setupdevice.presentation.CallerLineMode
import com.forematic.forelock.setupdevice.presentation.SetupDeviceEvent
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun CallerLineSetupSection(
    callerLineId: CallerLineIdentification,
    onEvent: (SetupDeviceEvent.CallerLineIdEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val canUpdateMode by remember(callerLineId.userMode, callerLineId.currentUserMode) {
        derivedStateOf { callerLineId.currentUserMode != callerLineId.userMode }
    }
    val canUpdateNumber by remember(callerLineId.number, callerLineId.location) {
        derivedStateOf {
            callerLineId.number.isNotBlank() && callerLineId.location.isNotBlank() &&
                    callerLineId.numberError == null && callerLineId.locationError == null
        }
    }

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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Choose user mode",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = callerLineId.currentUserMode.name,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChipWithToolTip(
                            isSelected = callerLineId.userMode == CallerLineMode.ANY,
                            onClick = {
                                onEvent(
                                    SetupDeviceEvent.CallerLineIdEvent.OnUserModeChange(
                                        CallerLineMode.ANY
                                    )
                                )
                            },
                            label = CallerLineMode.ANY.displayName
                        )

                        FilterChipWithToolTip(
                            isSelected = callerLineId.userMode == CallerLineMode.AUTHORIZED,
                            onClick = {
                                onEvent(
                                    SetupDeviceEvent.CallerLineIdEvent.OnUserModeChange(
                                        CallerLineMode.AUTHORIZED
                                    )
                                )
                            },
                            label = CallerLineMode.AUTHORIZED.displayName
                        )
                        IconButtonWithLoader(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Save",
                            onClick = {
                                if (!callerLineId.isUpdatingMode)
                                    onEvent(SetupDeviceEvent.CallerLineIdEvent.OnUpdateClick)
                            },
                            isLoading = callerLineId.isUpdatingMode,
                            isEnabled = canUpdateMode
                        )
                    }
                }

                CallerLineIdWithLocation(
                    callerLineId = callerLineId,
                    onNumberChange = {
                        onEvent(SetupDeviceEvent.CallerLineIdEvent.OnNumberChange(it))
                    },
                    onLocationChange = {
                        onEvent(SetupDeviceEvent.CallerLineIdEvent.OnLocationChange(it))
                    },
                    onFindAction = {
                        onEvent(SetupDeviceEvent.CallerLineIdEvent.OnFindLocation)
                    },
                    label = "Setup Authorized Call-In Number",
                    isEnabled = callerLineId.currentUserMode == CallerLineMode.AUTHORIZED
                )

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

                    ButtonWithLoadingIndicator(
                        onClick = {
                            if (!callerLineId.isUpdatingNumber)
                                onEvent(SetupDeviceEvent.CallerLineIdEvent.OnUpdateClick)
                        },
                        text = "Update",
                        isEnabled = canUpdateNumber,
                        isLoading = callerLineId.isUpdatingNumber,
                        modifier = Modifier.widthIn(min = 96.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CallerLineIdWithLocation(
    callerLineId: CallerLineIdentification,
    onNumberChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onFindAction: () -> Unit,
    label: String,
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
                value = callerLineId.location,
                onValueChange = onLocationChange,
                label = "Location",
                secondaryLabel = callerLineId.formatedLocationRange(),
                modifier = Modifier.widthIn(max = 148.dp),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    ButtonWithLoadingIndicator(
                        onClick = { if (!callerLineId.isFetchingLocation) onFindAction() },
                        text = "Find\nNext",
                        textStyle = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.height(IntrinsicSize.Max),
                        isEnabled = isEnabled,
                        isLoading = callerLineId.isFetchingLocation,
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                    )
                },
                isEnabled = isEnabled,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                )
            )

            LabeledTextField(
                value = callerLineId.number,
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
        ErrorTextWithDivider(
            text = callerLineId.locationError ?: callerLineId.numberError ?: ""
        )
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