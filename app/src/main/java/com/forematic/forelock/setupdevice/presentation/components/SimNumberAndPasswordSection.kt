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
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun SimNumberAndPasswordSection(
    simNumber: String,
    onSimNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onUpdateClick: () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    var canEditPassword by remember { mutableStateOf(false) }
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
                text = error ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp, end = 16.dp).align(Alignment.End)
            )
            Column(
                modifier = Modifier.padding(start = 16.dp, bottom = 24.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LabeledTextField(
                    label = "Enter SIM card number",
                    value = simNumber,
                    onValueChange = onSimNumberChange,
                    placeholder = "Eg. 01234567891",
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Call,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LabeledTextField(
                        label = "Programming password",
                        value = password,
                        onValueChange = onPasswordChange,
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

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.align(Alignment.Bottom)
                    ) {
                        Button(
                            onClick = onUpdateClick,
                            modifier = Modifier,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Update")
                        }
                    }
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
                simNumber = "",
                onSimNumberChange = { },
                password = "",
                onPasswordChange = { },
                onUpdateClick = { },
                error = "Password field must contain 4 digits",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}