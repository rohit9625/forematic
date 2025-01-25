package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    onUpdateClick: () -> Unit,
    modifier: Modifier = Modifier,
    isUpdatingTimzone: Boolean = false
) {
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
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimezoneMode.entries.forEach { mode ->
                    FilterChipWithToolTip(
                        isSelected = mode == selectedMode,
                        onClick = { onModeSelection(mode) },
                        label = mode.displayName,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(mode.icon),
                                contentDescription = null
                            )
                        },
                        toolTipText = mode.displayName,
                        modifier = Modifier.width(IntrinsicSize.Max)
                    )
                }
                ButtonWithLoadingIndicator(
                    onClick = onUpdateClick,
                    text = "Update",
                    isLoading = isUpdatingTimzone
                )
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
                onUpdateClick = { },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}