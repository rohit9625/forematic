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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    modifier: Modifier = Modifier,
    isUpdatingKeypadCodes: Boolean = false
) {
    val canUpdateCodes by remember(keypadCode1, keypadCode2, deliveryCode) {
        derivedStateOf { !keypadCode1.hasErrors() && !keypadCode2.hasErrors() && !deliveryCode.hasErrors() }
    }
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
                    keypadCode = keypadCode1,
                    onCodeChange = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnKeypadCode1Change(it))
                    },
                    onLocationChange = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnCodeLocation1Change(it))
                    },
                    onFindAction = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnFindKeypadCode1Location)
                    },
                    label = "Output 1 Code",
                    modifier = Modifier.fillMaxWidth()
                )

                KeypadCodeWithLocation(
                    keypadCode = keypadCode2,
                    onCodeChange = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnKeypadCode2Change(it))
                    },
                    onLocationChange = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnCodeLocation2Change(it))
                    },
                    onFindAction = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnFindKeypadCode2Location)
                    },
                    label = "Output 2 Code",
                    modifier = Modifier.fillMaxWidth()
                )
                
                KeypadCodeWithLocation(
                    keypadCode = deliveryCode,
                    onCodeChange = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnDeliveryCodeChange(it))
                    },
                    onLocationChange = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnDeliveryCodeLocationChange(it))
                    },
                    onFindAction = {
                        onEvent(SetupDeviceEvent.KeypadCodeEvent.OnFindDeliveryCodeLocation)
                    },
                    label = "Setup Delivery Code",
                    modifier = Modifier.fillMaxWidth()
                )

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
                    ButtonWithLoadingIndicator(
                        onClick = { onEvent(SetupDeviceEvent.KeypadCodeEvent.OnUpdateClick) },
                        text = "Update",
                        isLoading = isUpdatingKeypadCodes,
                        isEnabled = canUpdateCodes,
                        modifier = Modifier.widthIn(min = 96.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun KeypadCodeWithLocation(
    keypadCode: KeypadCodeForOutput,
    onCodeChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onFindAction: () -> Unit,
    label: String,
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
                value = keypadCode.location,
                onValueChange = onLocationChange,
                label = "Location",
                secondaryLabel = keypadCode.formatedLocationRange(),
                modifier = Modifier.widthIn(max = 156.dp),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    ButtonWithLoadingIndicator(
                        onClick = onFindAction,
                        text = "Find\nNext",
                        textStyle = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.height(IntrinsicSize.Max),
                        isLoading = keypadCode.isFetchingLocation,
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                )
            )

            LabeledTextField(
                value = keypadCode.code,
                onValueChange = onCodeChange,
                label = "Keypad Code",
                modifier = Modifier.widthIn(max = 128.dp),
                shape = RoundedCornerShape(12.dp),
                isError = keypadCode.codeError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                )
            )
        }

        ErrorTextWithDivider(text = keypadCode.locationError ?: keypadCode.codeError ?: "")
    }
}

@Preview
@Composable
private fun SetKeypadCodeSectionPreview() {
    ForeLockTheme {
        Surface {
            SetKeypadCodeSection(
                keypadCode1 = KeypadCodeForOutput(locationRange = 1..100),
                keypadCode2 = KeypadCodeForOutput(locationRange = 101..150),
                deliveryCode = KeypadCodeForOutput(locationRange = 151..200),
                onEvent = { },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }
}