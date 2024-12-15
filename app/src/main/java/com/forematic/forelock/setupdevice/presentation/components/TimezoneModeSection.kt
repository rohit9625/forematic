package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.setupdevice.presentation.TimezoneMode
import com.forematic.forelock.ui.theme.ForeLockTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimezoneModeSection(
    selectedMode: TimezoneMode?,
    onModeSelection: (TimezoneMode) -> Unit,
    modifier: Modifier = Modifier
) {
    var tooltipMode: TimezoneMode? by remember { mutableStateOf(null) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = "Timezone Mode",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
        Card {
            FlowRow(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimezoneMode.entries.forEach { mode ->
                    Row(
                        modifier = Modifier.width(IntrinsicSize.Max)
                    ) {
                        FilterChip(
                            selected = mode == selectedMode,
                            onClick = { onModeSelection(mode) },
                            label = { Text(text = mode.displayName) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(mode.icon),
                                    contentDescription = null
                                )
                            },
                        )

                        ToolTipWithIcon(
                            showToolTip = mode == tooltipMode,
                            onClick = { tooltipMode = mode },
                            onDismiss = { tooltipMode = null },
                            infoText = mode.displayName
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TimezoneModeSectionPreview() {
    ForeLockTheme {
        Surface {
            TimezoneModeSection(
                selectedMode = null,
                onModeSelection = { },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}