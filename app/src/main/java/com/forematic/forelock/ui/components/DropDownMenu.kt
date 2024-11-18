package com.forematic.forelock.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.forematic.forelock.ui.screens.DeviceType
import com.forematic.forelock.ui.screens.OutputRelayText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDown(
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    options: List<String> = emptyList()
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { /*TODO("No use of this")*/ },
            modifier = Modifier
                .height(50.dp)
                .menuAnchor(),
            textStyle = MaterialTheme.typography.titleSmall,
            readOnly = true,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
        )

        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { onExpandedChange(false) }) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = { onValueChange(item) },
                    enabled = true
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDown(
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    value: DeviceType,
    onValueChange: (DeviceType) -> Unit,
    modifier: Modifier = Modifier,
    options: List<DeviceType> = emptyList()
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value.displayName,
            onValueChange = { /*TODO("No use of this")*/ },
            modifier = Modifier
                .height(50.dp)
                .menuAnchor(),
            textStyle = MaterialTheme.typography.titleSmall,
            readOnly = true,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
        )

        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { onExpandedChange(false) }) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.displayName) },
                    onClick = { onValueChange(item) },
                    enabled = true
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDown(
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    value: OutputRelayText,
    onValueChange: (OutputRelayText) -> Unit,
    modifier: Modifier = Modifier,
    options: List<OutputRelayText> = emptyList()
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value.displayName,
            onValueChange = { /*TODO("No use of this")*/ },
            modifier = Modifier
                .height(50.dp)
                .menuAnchor(),
            textStyle = MaterialTheme.typography.titleSmall,
            readOnly = true,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
        )

        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { onExpandedChange(false) }) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.displayName) },
                    onClick = { onValueChange(item) },
                    enabled = true
                )
            }
        }
    }
}