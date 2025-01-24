package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.forematic.forelock.R
import com.forematic.forelock.ui.theme.ForeLockTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectGateIconDialog(
    onSelectIcon: (icon: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val icons = listOf(
        R.drawable.ic_side_gate,
        R.drawable.ic_parking_gate,
        R.drawable.ic_office_gate,
        R.drawable.ic_garage_gate,
        R.drawable.ic_big_gate_brown,
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Select Appropriate Gate Icon",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                FlowRow(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    maxItemsInEachRow = 3
                ) {
                    icons.forEach {
                        Card(
                            onClick = { onSelectIcon(it); onDismiss() },
                            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                painter = painterResource(it),
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp).size(48.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SelectGateIconDialogPreview() {
    ForeLockTheme {
        Surface {
            SelectGateIconDialog(onSelectIcon = { }, onDismiss = {})
        }
    }
}