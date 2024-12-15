package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderWithIndicator(
    steps: Int = 0,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    val interactionSource = remember { MutableInteractionSource() }
    val sliderState = remember {
        SliderState(
            valueRange = valueRange,
            onValueChangeFinished = { /*TODO("Perform some action with updated value")*/},
            steps = steps
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Slider(
            state = sliderState,
            modifier = Modifier.weight(0.9f),
            interactionSource = interactionSource,
            thumb = {
                SliderDefaults.Thumb(interactionSource = interactionSource)
            },
            track = { SliderDefaults.Track(sliderState = sliderState) }
        )

        Text(
            text = "%.0f".format(sliderState.value),
            modifier = Modifier.weight(0.1f),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}