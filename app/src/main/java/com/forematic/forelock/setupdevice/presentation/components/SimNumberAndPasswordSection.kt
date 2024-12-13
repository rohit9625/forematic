package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.setupdevice.presentation.SetupDeviceEvent
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun SimNumberAndPasswordSection(modifier: Modifier = Modifier) {
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
            Column(
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LabeledTextField(
                    label = "Enter SIM card number",
                    value = "",
                    onValueChange = {  },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LabeledTextField(
                        label = "Programming password",
                        value = "",
                        onValueChange = {  },
                        modifier = Modifier.width(164.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.align(Alignment.Bottom)
                    ) {
                        Button(
                            onClick = { },
                            modifier = Modifier,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Get ID")
                        }
//                                ToolTipWithIcon(
//                                    showToolTip = isToolTipVisible,
//                                    onClick = { isToolTipVisible = true },
//                                    onDismiss = { isToolTipVisible = false },
//                                    infoText = "Read from device"
//                                )
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
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            )
        }
    }
}