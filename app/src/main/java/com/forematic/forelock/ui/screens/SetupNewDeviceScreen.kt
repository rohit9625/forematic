package com.forematic.forelock.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forematic.forelock.ui.theme.ForeLockTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupNewDeviceScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Setup New Device",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp))
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            ListItem(
                headlineContent = {
                    var isExpanded by remember { mutableStateOf(false) }
                    var deviceType by remember { mutableStateOf("Device Type") }

                    ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it}) {
                        OutlinedTextField(
                            value = deviceType,
                            onValueChange = { deviceType = it },
                            modifier = Modifier.height(50.dp),
                            textStyle = MaterialTheme.typography.titleSmall,
                            readOnly = true,
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            shape = RoundedCornerShape(12.dp),
                        )
                        
                        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false}) {
                            DropdownMenuItem(
                                text = { Text(text = "Device Type") },
                                onClick = {
                                    deviceType = "Device Type"
                                    isExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "G24") },
                                onClick = {
                                    deviceType = "G24"
                                    isExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "G64") },
                                onClick = {
                                    deviceType = "G64"
                                    isExpanded = false
                                }
                            )
                        }
                    }
                },
                overlineContent = {
                    Text(text = "Choose the device type")
                }
            )

            ListItem(
                headlineContent = {
                    Text(text = "Device Type")
                },
                overlineContent = {
                    Text(text = "Enter SIM card number")
                }
            )

            ListItem(
                headlineContent = {
                    Text(text = "Device Type")
                },
                overlineContent = {
                    Text(text = "Provide an output naming")
                }
            )
        }
    }
}

@Preview
@Composable
private fun SetupNewDeviceScreenPreview() {
    ForeLockTheme {
        SetupNewDeviceScreen(onNavigateBack = { })
    }
}