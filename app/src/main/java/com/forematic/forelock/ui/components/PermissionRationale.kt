package com.forematic.forelock.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun PermissionRationale(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onConfirm: ()-> Unit,
    onDismiss: ()-> Unit,
    onGoToAppSettings: ()-> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = modifier) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Permission Required",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = permissionTextProvider.getDescription(isPermanentlyDeclined),
                    style = MaterialTheme.typography.bodyMedium
                )

            }
            HorizontalDivider()
            Row {
                if(isPermanentlyDeclined) {
                    TextButton(
                        onClick = onGoToAppSettings,
                        modifier = Modifier
                            .weight(1f)
                            .height(IntrinsicSize.Min),
                        shape = RoundedCornerShape(bottomStart = 12.dp)
                    ) {
                        Text(text = "Go To Settings".uppercase())
                    }
                } else {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(IntrinsicSize.Min),
                        shape = RoundedCornerShape(bottomStart = 12.dp)
                    ) {
                        Text(text = "Cancel".uppercase())
                    }
                    TextButton(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(IntrinsicSize.Min),
                        shape = RoundedCornerShape(bottomEnd = 12.dp)
                    ) {
                        Text(text = "Ok".uppercase())
                    }
                }
            }
        }
    }
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class SmsPermissionText: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "You have permanently declined the SEND_SMS permission. " +
                    "To operate the gate, please enable the permission in your app settings."
        } else {
            "The app requires SEND_SMS permission to send codes for operating the gate. " +
                    "Please grant this permission to continue."
        }
    }
}

@Preview
@Composable
private fun PermissionDialogPreview() {
    ForeLockTheme {
        Surface(tonalElevation = 5.dp) {
            PermissionRationale(
                permissionTextProvider = SmsPermissionText(),
                isPermanentlyDeclined = true,
                onConfirm = {},
                onDismiss = {},
                onGoToAppSettings = {},
                modifier = Modifier
            )
        }
    }
}