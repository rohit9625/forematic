package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.setupdevice.presentation.CallOutNumber
import com.forematic.forelock.setupdevice.presentation.SetupDeviceEvent
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun CallOutNumberSection(
    callOutNumbers: List<CallOutNumber>,
    onEvent: (SetupDeviceEvent.CallOutNumberEvent) -> Unit,
    modifier: Modifier = Modifier,
    canAddMoreNumbers: Boolean = true
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
            Box {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(top = 24.dp, start = 16.dp, bottom = 8.dp, end = 16.dp)
                        .fillMaxWidth()
                ) {
                    callOutNumbers.forEachIndexed { index, item->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${index + 1}.")
                            LabeledTextField(
                                label = if(index == 0) "Admin Number" else "Call-out Number",
                                value = item.number,
                                onValueChange = {
                                    onEvent(
                                        SetupDeviceEvent.CallOutNumberEvent.UpdateNumber(index, it)
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Call,
                                        contentDescription = null
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier.weight(0.5f)
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
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Person,
                                        contentDescription = null
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier.weight(0.5f)
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
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
                            modifier = Modifier
                        )
                        Button(
                            onClick = {
                                onEvent(SetupDeviceEvent.CallOutNumberEvent.OnUpdateClick)
                            },
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(text = "Update")
                        }
                    }
                }
                ToolTipWithIcon(
                    showToolTip = isToolTipVisible,
                    onClick = { isToolTipVisible = true },
                    onDismiss = { isToolTipVisible = false },
                    infoText = "If a call is not answered, it redirects call to the next number the list.",
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CallOutNumberSectionPreview() {
    ForeLockTheme {
        Surface {
            CallOutNumberSection(
                callOutNumbers = listOf(CallOutNumber(number = "1234567890", name = "Test")),
                onEvent = { },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}