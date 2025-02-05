package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun AudioAdjustmentSection(
    speakerVolume: Float,
    micVolume: Float,
    signalStrength: Int?,
    onSpeakerVolumeChange: (Float) -> Unit,
    onMicVolumeChange: (Float) -> Unit,
    onCheckSignalStrength: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = "Audio Adjustments & Signal Strength",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Panel Speaker Volume(0-5)",
                        style = MaterialTheme.typography.labelMedium
                    )
                    SliderWithIndicator(
                        value = speakerVolume,
                        onValueChange = onSpeakerVolumeChange,
                        steps = 4,
                        valueRange = 0f..5f
                    )
                }

                HorizontalDivider()

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Panel Microphone Volume(0-8)",
                        style = MaterialTheme.typography.labelMedium
                    )
                    SliderWithIndicator(
                        value = micVolume,
                        onValueChange = onMicVolumeChange,
                        steps = 7,
                        valueRange = 0f..8f
                    )
                }

                HorizontalDivider()

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Strength: $signalStrength",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "(1-31)",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = onCheckSignalStrength,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Check Signal Strength",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AudioAdjustmentSectionPreview() {
    ForeLockTheme {
        Surface {
            AudioAdjustmentSection(
                speakerVolume = 3f,
                micVolume = 6f,
                signalStrength = 24,
                onSpeakerVolumeChange = { },
                onMicVolumeChange = { },
                onCheckSignalStrength = { },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}