package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.R
import com.forematic.forelock.setupdevice.presentation.CallOutNumberUiState
import com.forematic.forelock.setupdevice.presentation.SetupDeviceEvent
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun CallOutNumberSection(
    firstCallOut: CallOutNumberUiState,
    secondCallOut: CallOutNumberUiState,
    thirdCallOut: CallOutNumberUiState,
    adminNumber: String,
    onEvent: (SetupDeviceEvent.CallOutNumberEvent) -> Unit,
    modifier: Modifier = Modifier,
    adminNumberError: String? = null,
    isUpdatingCallOutNumbers: Boolean = false,
    isUpdatingAdminNumber: Boolean = false
) {
    var isToolTipVisible by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = "Call Out Numbers",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
        Card {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Last 6 digits will be used for identification",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    ToolTipWithIcon(
                        showToolTip = isToolTipVisible,
                        onClick = { isToolTipVisible = true },
                        onDismiss = { isToolTipVisible = false },
                        infoText = "If a call is not answered, it redirects call to the next number the list.",
                        modifier = Modifier
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CallOutNumberWithName(
                        name = firstCallOut.name,
                        onNameChange = {
                            onEvent(SetupDeviceEvent.CallOutNumberEvent.OnFirstNameChange(it))
                        },
                        number = firstCallOut.number,
                        onNumberChange = {
                            onEvent(SetupDeviceEvent.CallOutNumberEvent.OnFirstNumberChange(it))
                        },
                        error = firstCallOut.numberError ?: firstCallOut.nameError,
                        label = "1. Call-Out Number",
                        modifier = Modifier.fillMaxWidth()
                    )

                    CallOutNumberWithName(
                        name = secondCallOut.name,
                        onNameChange = {
                            onEvent(SetupDeviceEvent.CallOutNumberEvent.OnSecondNameChange(it))
                        },
                        number = secondCallOut.number,
                        onNumberChange = {
                            onEvent(SetupDeviceEvent.CallOutNumberEvent.OnSecondNumberChange(it))
                        },
                        error = secondCallOut.numberError ?: secondCallOut.nameError,
                        label = "1. Call-Out Number",
                        modifier = Modifier.fillMaxWidth()
                    )

                    CallOutNumberWithName(
                        name = thirdCallOut.name,
                        onNameChange = {
                            onEvent(SetupDeviceEvent.CallOutNumberEvent.OnThirdNameChange(it))
                        },
                        number = thirdCallOut.number,
                        onNumberChange = {
                            onEvent(SetupDeviceEvent.CallOutNumberEvent.OnThirdNumberChange(it))
                        },
                        error = thirdCallOut.numberError ?: thirdCallOut.nameError,
                        label = "1. Call-Out Number",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    OutlinedButton(
                        onClick = { },
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(text = "From Contacts")
                    }

                    ButtonWithLoadingIndicator(
                        onClick = {
                            if(!isUpdatingCallOutNumbers)
                                onEvent(SetupDeviceEvent.CallOutNumberEvent.OnUpdateClick)
                        },
                        text = "Update",
                        isLoading = isUpdatingCallOutNumbers,
                        modifier = Modifier.widthIn(min = 96.dp)
                    )
                }

                Column {
                    HorizontalDivider()

                    ErrorText(
                        text = adminNumberError ?: "",
                        modifier = Modifier.align(Alignment.End)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LabeledTextField(
                            label = "Change Admin Number",
                            value = adminNumber,
                            onValueChange = {
                                onEvent(SetupDeviceEvent.CallOutNumberEvent.OnAdminNumberChange(it))
                            },
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_phone_person_24),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.widthIn(max = 200.dp)
                        )

                        ButtonWithLoadingIndicator(
                            onClick = {
                                if(!isUpdatingAdminNumber)
                                    onEvent(SetupDeviceEvent.CallOutNumberEvent.OnChangeClick)
                            },
                            text = "Change",
                            isLoading = isUpdatingAdminNumber,
                            modifier = Modifier.widthIn(min = 96.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CallOutNumberWithName(
    number: String,
    onNumberChange: (String) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    label: String = "Call-Out Number"
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            LabeledTextField(
                label = label,
                value = number,
                onValueChange = onNumberChange,
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_call_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(0.55f)
            )

            LabeledTextField(
                label = "Name",
                value = name,
                onValueChange = onNameChange,
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_person_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.weight(0.45f)
            )
        }
        ErrorTextWithDivider(
            text = error ?: ""
        )
    }
}

@Preview
@Composable
private fun CallOutNumberSectionPreview() {
    ForeLockTheme {
        Surface {
            CallOutNumberSection(
                firstCallOut = CallOutNumberUiState(nameError = "Name cannot be empty"),
                secondCallOut = CallOutNumberUiState(),
                thirdCallOut = CallOutNumberUiState(),
                adminNumber = "",
                onEvent = { },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}