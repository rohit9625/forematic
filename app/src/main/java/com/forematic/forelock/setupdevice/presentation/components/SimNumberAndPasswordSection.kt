package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.R
import com.forematic.forelock.setupdevice.presentation.SetupDeviceEvent
import com.forematic.forelock.setupdevice.presentation.SimAndPasswordState
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun SimNumberAndPasswordSection(
    simAndPasswordState: SimAndPasswordState,
    currentProgrammingPassword: String,
    onEvent: (SetupDeviceEvent.SimAndPasswordEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var canEditPassword by remember { mutableStateOf(false) }
    val canUpdate by remember(simAndPasswordState) {
        derivedStateOf { simAndPasswordState.simNumberError == null
                && simAndPasswordState.passwordError == null
        }
    }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(canEditPassword) {
        if(canEditPassword) focusRequester.requestFocus()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = "G24 SIM and Password",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 8.dp)
        )

        Card {
            Text(
                text = simAndPasswordState.simNumberError ?: simAndPasswordState.passwordError ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .align(Alignment.End)
            )
            Column(
                modifier = Modifier.padding(start = 16.dp, bottom = 24.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LabeledTextField(
                        label = "Enter SIM card number",
                        value = simAndPasswordState.simNumber,
                        onValueChange = {
                            onEvent(SetupDeviceEvent.SimAndPasswordEvent.OnSimNumberChange(it))
                        },
                        placeholder = "Eg. 01234567891",
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_dialer_sim_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        isError = simAndPasswordState.simNumberError != null,
                        modifier = Modifier.weight(0.7f)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(0.3f)
                    ) {
                        Text(
                            text = currentProgrammingPassword,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Current Password",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LabeledTextField(
                        label = "Programming password",
                        value = simAndPasswordState.programmingPassword,
                        onValueChange = {
                            onEvent(SetupDeviceEvent.SimAndPasswordEvent.OnPasswordChange(it))
                        },
                        modifier = Modifier.width(164.dp),
                        trailingIcon = {
                            FilledIconButton(
                                onClick = { canEditPassword = true; focusRequester.requestFocus() },
                                shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Edit,
                                    contentDescription = null
                                )
                            }
                        },
                        isEnabled = canEditPassword,
                        focusRequester = focusRequester,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters
                        )
                    )

                    ButtonWithLoadingIndicator(
                        onClick = {
                            onEvent(SetupDeviceEvent.SimAndPasswordEvent.OnUpdateClick)
                            canEditPassword = false
                        },
                        text = "Update",
                        isLoading = simAndPasswordState.isLoading,
                        isEnabled = canUpdate
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SimNumberAndPasswordSectionPreview() {
    ForeLockTheme {
        Surface {
            SimNumberAndPasswordSection(
                simAndPasswordState = SimAndPasswordState(),
                currentProgrammingPassword = "1234",
                onEvent = { },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}