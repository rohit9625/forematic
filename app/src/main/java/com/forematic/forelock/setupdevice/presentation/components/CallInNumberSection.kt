package com.forematic.forelock.setupdevice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
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
import com.forematic.forelock.R
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun CallInNumberSection(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Card {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Choose user mode",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChipWithToolTip(
                            isSelected = false,
                            onClick = { },
                            label = "Any"
                        )

                        FilterChipWithToolTip(
                            isSelected = true,
                            onClick = { },
                            label = "Authorized"
                        )
                    }
                }

                CallInNumberWithLocation(
                    number = "",
                    onNumberChange = { },
                    location = "",
                    onLocationChange = { },
                    onFindAction = { },
                    label = "Setup Authorized Call-In Number",
                    locationRange = "200-250"
                )

                Text(
                    text = "Enter the last 8 digits of the caller's number",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun CallInNumberWithLocation(
    number: String,
    onNumberChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    onFindAction: () -> Unit,
    label: String,
    locationRange: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            LabeledTextField(
                value = location,
                onValueChange = onLocationChange,
                label = "Location",
                secondaryLabel = locationRange,
                modifier = Modifier.widthIn(max = 148.dp),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    Button(
                        onClick = onFindAction,
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                        contentPadding = PaddingValues(),
                        modifier = Modifier.height(IntrinsicSize.Max)
                    ) {
                        Text(
                            text = "Find\nNext",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            )

            LabeledTextField(
                value = number,
                onValueChange = onNumberChange,
                label = "Caller Number",
                modifier = Modifier.widthIn(max = 172.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Preview
@Composable
private fun DialInNumberSectionPreview() {
    ForeLockTheme {
        Surface {
            CallInNumberSection(
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}