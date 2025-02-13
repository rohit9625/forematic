package com.forematic.forelock.g64setupscreen.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.forelock.home.presentation.components.IconButtonWithLoader
import com.forematic.forelock.setupdevice.presentation.CallerLineMode
import com.forematic.forelock.setupdevice.presentation.SetupDeviceEvent
import com.forematic.forelock.setupdevice.presentation.components.ButtonWithLoadingIndicator
import com.forematic.forelock.setupdevice.presentation.components.FilterChipWithToolTip
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun SettingSection(
    signalStrength: Int?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large
    ) {
       Column(
           verticalArrangement = Arrangement.spacedBy(12.dp),
           modifier = Modifier.padding(16.dp)
       ) {
           Column(
               verticalArrangement = Arrangement.spacedBy(8.dp)
           ) {
               Row(
                   modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceBetween
               ) {
                   Text(
                       text = "Set Call-in mode",
                       style = MaterialTheme.typography.labelMedium,
                       color = MaterialTheme.colorScheme.primary
                   )
                   Text(
                       text = "Authorized",
                       style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                       color = MaterialTheme.colorScheme.tertiary
                   )
               }
               Row(
                   horizontalArrangement = Arrangement.SpaceBetween,
                   modifier = Modifier.fillMaxWidth()
               ) {
                   FilterChipWithToolTip(
                       isSelected = false,
                       onClick = {
                           /*TODO()*/
                       },
                       label = CallerLineMode.ANY.displayName
                   )

                   FilterChipWithToolTip(
                       isSelected = true,
                       onClick = {
                           /*TODO()*/
                       },
                       label = CallerLineMode.AUTHORIZED.displayName
                   )
                   IconButtonWithLoader(
                       imageVector = Icons.Default.Done,
                       contentDescription = "Save",
                       onClick = {
                           /*TODO()*/
                       }
                   )
               }
           }

           HorizontalDivider()

           Column(
               verticalArrangement = Arrangement.spacedBy(8.dp)
           ) {
               Row(
                   modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceBetween
               ) {
                   Text(
                       text = "Notification by SMS",
                       style = MaterialTheme.typography.labelMedium,
                       color = MaterialTheme.colorScheme.primary
                   )
                   Text(
                       text = "Enabled",
                       style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                       color = MaterialTheme.colorScheme.tertiary
                   )
               }
               Row(
                   horizontalArrangement = Arrangement.SpaceBetween,
                   modifier = Modifier.fillMaxWidth()
               ) {
                   FilterChipWithToolTip(
                       isSelected = true,
                       onClick = {
                           /*TODO()*/
                       },
                       label = "Enable"
                   )

                   FilterChipWithToolTip(
                       isSelected = false,
                       onClick = {
                           /*TODO()*/
                       },
                       label = "Disable"
                   )
                   IconButtonWithLoader(
                       imageVector = Icons.Default.Done,
                       contentDescription = "Save",
                       onClick = {
                           /*TODO()*/
                       }
                   )
               }
           }

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
                   onClick = {  },
                   text = "Refresh",
                   isLoading = false,
                   modifier = Modifier.widthIn(min = 96.dp)
               )
           }
       }
    }
}

@Preview
@Composable
private fun SettingSectionPreview() {
    ForeLockTheme {
        Surface {
            SettingSection(
                signalStrength = 4,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}