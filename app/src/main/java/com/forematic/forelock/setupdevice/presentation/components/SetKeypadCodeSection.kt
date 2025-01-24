package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.setupdevice.presentation.KeypadCodeForOutput
import com.forematic.forelock.setupdevice.presentation.SetupDeviceEvent
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun SetKeypadCodeSection(
    keypadCode1: KeypadCodeForOutput,
    keypadCode2: KeypadCodeForOutput,
    deliveryCode: KeypadCodeForOutput,
    onEvent: (SetupDeviceEvent.KeypadCodeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = "Set Keypad Code(2-8 Digits)",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                KeypadCodeWithLocation(
                    code = keypadCode1.code,
                    onCodeChange = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnKeypadCode1Change(it))
                    },
                    location = keypadCode1.location,
                    onLocationChange = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnCodeLocation1Change(it))
                    },
                    onFindAction = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnFindKeypadCode1Location)
                    },
                    label = "Output 1 Code",
                    locationRange = keypadCode1.locationRange,
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider()

                KeypadCodeWithLocation(
                    code = keypadCode2.code,
                    onCodeChange = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnKeypadCode2Change(it))
                    },
                    location = keypadCode2.location,
                    onLocationChange = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnCodeLocation2Change(it))
                    },
                    onFindAction = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnFindKeypadCode2Location)
                    },
                    label = "Output 2 Code",
                    locationRange = keypadCode2.locationRange,
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider()
                
                KeypadCodeWithLocation(
                    code = deliveryCode.code,
                    onCodeChange = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnDeliveryCodeChange(it))
                    },
                    location = deliveryCode.location,
                    onLocationChange = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnDeliveryCodeLocationChange(it))
                    },
                    onFindAction = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnFindDeliveryCodeLocation)
                    },
                    label = "Setup Delivery Code",
                    locationRange = deliveryCode.locationRange,
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider()

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "The delivery code will self delete after it is used",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.widthIn(max = 200.dp)
                    )
                    Button(
                        onClick = {
                            onEvent(SetupDeviceEvent.KeypadCodeEvent.OnUpdateClick)
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
fun KeypadCodeWithLocation(
    code: String,
    onCodeChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    onFindAction: () -> Unit,
    label: String,
    locationRange: String,
    modifier: Modifier = Modifier
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
                modifier = Modifier.widthIn(max = 156.dp),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    Button(
                        onClick = onFindAction,
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                        contentPadding = PaddingValues(),
                        modifier = Modifier.height(IntrinsicSize.Max)
                    ) {
                        Text(
                            text = "Find\nNext",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                )
            )

            LabeledTextField(
                value = code,
                onValueChange = onCodeChange,
                label = "Keypad Code",
                modifier = Modifier.widthIn(max = 128.dp),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                )
            )
        }
    }
}

@Preview
@Composable
private fun SetKeypadCodeSectionPreview() {
    ForeLockTheme {
        Surface {
            Scaffold { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SetKeypadCodeSection(
                        keypadCode1 = KeypadCodeForOutput(),
                        keypadCode2 = KeypadCodeForOutput(),
                        deliveryCode = KeypadCodeForOutput(),
                        onEvent = { },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}