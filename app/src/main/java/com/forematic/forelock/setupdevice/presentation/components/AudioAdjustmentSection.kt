package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.R
import com.forematic.forelock.home.presentation.components.IconButtonWithLoader
import com.forematic.forelock.setupdevice.presentation.VolumeSettings
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun AudioAdjustmentSection(
    volumeSettings: VolumeSettings,
    signalStrength: Int?,
    onSpeakerVolumeChange: (Float) -> Unit,
    onMicVolumeChange: (Float) -> Unit,
    onCheckSignalStrength: () -> Unit,
    modifier: Modifier = Modifier,
    isRefreshingSignal: Boolean = false
) {
    val canUpdateMicVolume by remember(volumeSettings) {
        derivedStateOf { volumeSettings.currentMicVolume != volumeSettings.micVolume }
    }
    val canUpdateSpeakerVolume by remember(volumeSettings) {
        derivedStateOf { volumeSettings.currentSpeakerVolume != volumeSettings.speakerVolume }
    }

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
                VolumeControls(
                    volume = volumeSettings.speakerVolume,
                    onVolumeChange = onSpeakerVolumeChange,
                    onUpdateVolume = onCheckSignalStrength,
                    isUpdatingVolume = volumeSettings.isUpdatingSpeakerVolume,
                    currentVolume = volumeSettings.currentSpeakerVolume,
                    isUpdateButtonEnabled = canUpdateSpeakerVolume,
                    volumeRange = 0f..5f,
                    steps = 4,
                    label = "Panel Speaker Volume(0-5)",
                    indicatorIcon = painterResource(R.drawable.ic_outline_speaker_phone_24)
                )

                HorizontalDivider()

                VolumeControls(
                    volume = volumeSettings.micVolume,
                    onVolumeChange = onMicVolumeChange,
                    onUpdateVolume = onCheckSignalStrength,
                    isUpdatingVolume = volumeSettings.isUpdatingMicVolume,
                    currentVolume = volumeSettings.currentMicVolume,
                    isUpdateButtonEnabled = canUpdateMicVolume,
                    volumeRange = 0f..8f,
                    steps = 7,
                    label = "Panel Mic Volume(0-8)",
                    indicatorIcon = painterResource(R.drawable.ic_outline_mic_24)
                )

                HorizontalDivider()

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Signal Strength: $signalStrength",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "(1-5)",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    ButtonWithLoadingIndicator(
                        onClick = onCheckSignalStrength,
                        text = "Refresh",
                        isLoading = isRefreshingSignal,
                        modifier = Modifier.widthIn(min = 96.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VolumeControls(
    volume: Float,
    onVolumeChange: (Float) -> Unit,
    onUpdateVolume: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Volume Controls",
    steps: Int = 0,
    volumeRange: ClosedFloatingPointRange<Float> = 0f..1f,
    isUpdatingVolume: Boolean = false,
    isUpdateButtonEnabled: Boolean = true,
    currentVolume: Float = 0f,
    indicatorIcon: Painter? = null
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "%.0f".format(currentVolume),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
                indicatorIcon?.let {
                    Icon(
                        painter = it,
                        contentDescription = "Mic Volume",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
        Row {
            SliderWithIndicator(
                value = volume,
                onValueChange = onVolumeChange,
                steps = steps,
                valueRange = volumeRange,
                modifier = Modifier.weight(1f)
            )
            IconButtonWithLoader(
                imageVector = Icons.Default.Done,
                contentDescription = "Save",
                onClick = onUpdateVolume,
                isLoading = isUpdatingVolume,
                isEnabled = isUpdateButtonEnabled
            )
        }
    }
}

@Preview
@Composable
private fun AudioAdjustmentSectionPreview() {
    ForeLockTheme {
        Surface {
            AudioAdjustmentSection(
                volumeSettings = VolumeSettings(
                    speakerVolume = 3f, micVolume = 7f,
                    currentMicVolume = 3f, currentSpeakerVolume = 5f
                ),
                signalStrength = 5,
                onSpeakerVolumeChange = { },
                onMicVolumeChange = { },
                onCheckSignalStrength = { },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}