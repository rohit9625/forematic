package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
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
import com.forematic.forelock.R
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun FilterChipWithToolTip(
    isSelected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable() (() -> Unit)? = null,
    toolTipText: String = "Add a brief explanation"
) {
    var isToolTipVisible by remember { mutableStateOf(false) }

    Row(modifier = modifier.width(IntrinsicSize.Max)) {
        FilterChip(
            selected = isSelected,
            onClick = onClick,
            label = { Text(text = label) },
            leadingIcon = leadingIcon
        )

        ToolTipWithIcon(
            showToolTip = isToolTipVisible,
            onClick = { isToolTipVisible = true },
            onDismiss = { isToolTipVisible = false },
            infoText = toolTipText
        )
    }
}

@Preview
@Composable
private fun FilterChipWithToolTipPreview() {
    ForeLockTheme {
        Surface {
            FilterChipWithToolTip(
                isSelected = true,
                onClick = { },
                label = "Click Me!",
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_free_24),
                        contentDescription = null
                    )
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}