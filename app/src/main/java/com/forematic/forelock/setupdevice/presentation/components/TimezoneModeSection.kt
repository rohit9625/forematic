package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import com.forematic.forelock.setupdevice.presentation.TimezoneMode
import com.forematic.forelock.ui.theme.ForeLockTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimezoneModeSection(
    selectedMode: TimezoneMode,
    currentMode: TimezoneMode,
    onModeSelection: (TimezoneMode) -> Unit,
    onUpdateClick: () -> Unit,
    modifier: Modifier = Modifier,
    isUpdatingTimezone: Boolean = false,
    error: String? = null,
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
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
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
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            toolTipText = mode.displayName,
                            modifier = Modifier.widthIn(min = 148.dp)
                        )
                    }
                }
                ErrorTextWithDivider(
                    text = error ?: "",
                    modifier = Modifier.align(Alignment.End)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Current Timezone",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = currentMode.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    ButtonWithLoadingIndicator(
                        onClick = onUpdateClick,
                        text = "Update",
                        isLoading = isUpdatingTimezone,
                        modifier = Modifier.widthIn(min = 96.dp)
                    )
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
                selectedMode = TimezoneMode.HOLIDAY,
                currentMode = TimezoneMode.HOLIDAY,
                onModeSelection = { },
                onUpdateClick = { },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}