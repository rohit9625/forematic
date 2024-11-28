package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LabeledBox(
    label: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .border(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(16.dp)
                ),
        ) {
            content()
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .offset(x = 16.dp, y = (-8).dp)
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(horizontal = 4.dp)
        )
    }
}