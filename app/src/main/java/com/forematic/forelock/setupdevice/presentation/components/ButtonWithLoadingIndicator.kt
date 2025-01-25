package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun ButtonWithLoadingIndicator(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.widthIn(min = 108.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        if(isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(text = "Update".uppercase())
        }
    }
}

@Preview
@Composable
private fun ButtonWithLoadingIndicatorPreview() {
    var isLoading by remember { mutableStateOf(false) }

    ForeLockTheme {
        Surface {
            ButtonWithLoadingIndicator(
                onClick = { isLoading = !isLoading },
                isLoading = isLoading,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}