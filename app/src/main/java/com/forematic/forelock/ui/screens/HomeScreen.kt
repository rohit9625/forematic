package com.forematic.forelock.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.forematic.forelock.R
import com.forematic.forelock.ui.theme.SignalGSMTheme
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    hasPermission: ()-> Boolean,
    requestPermission: () -> Unit
) {
    val navigationItems = listOf(
        NavigationItem("Home", R.drawable.ic_filled_home, R.drawable.ic_outline_home),
        NavigationItem("Keypad", R.drawable.ic_baseline_dialpad, R.drawable.ic_baseline_dialpad),
        NavigationItem("Phone", R.drawable.ic_baseline_phone, R.drawable.ic_baseline_phone),
        NavigationItem("More", R.drawable.ic_baseline_more, R.drawable.ic_baseline_more)
    )
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                navigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = { selectedItemIndex = index },
                        icon = {
                            Icon(
                                painter = painterResource(item.selectedIcon),
                                contentDescription = item.title
                            )
                        },
                        label = { Text(text = item.title) },
                        alwaysShowLabel = false,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (hasPermission()) {
                        onEvent(HomeScreenEvent.UpdateDialogVisibility(true))
                    } else {
                        requestPermission()
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Open".uppercase(),
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                )
            }
        }
    }

    if (uiState.showOpenGateDialog && hasPermission()) {
        OpenGateDialog(
            pinNumber = uiState.pinNumber,
            telephoneNumber = uiState.targetGsmNumber,
            onPinNumberChange = { onEvent(HomeScreenEvent.OnPinChange(it)) },
            onTelephoneNumberChange = { onEvent(HomeScreenEvent.OnTargetNumberChange(it)) },
            errorMessage = uiState.errorMessage,
            onDismissRequest = { onEvent(HomeScreenEvent.UpdateDialogVisibility(false)) },
            onSendAction = {
                onEvent(HomeScreenEvent.OnSendSignal {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message = "Signal sent to the gate")
                    }
                })
            }
        )
    }
}

@Composable
fun OpenGateDialog(
    pinNumber: String,
    telephoneNumber: String,
    onPinNumberChange: (String) -> Unit,
    onTelephoneNumberChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onSendAction: () -> Unit,
    errorMessage: String? = null,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        var isPasswordVisible by remember { mutableStateOf(false) }
        val passwordIcon = if (isPasswordVisible) painterResource(R.drawable.ic_visibility_on)
        else painterResource(R.drawable.ic_visibility_off)

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Enter the details to open the gate",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .fillMaxWidth(),
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedTextField(
                    value = pinNumber,
                    onValueChange = onPinNumberChange,
                    label = { Text(text = "Pin") },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                painter = passwordIcon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = telephoneNumber,
                    onValueChange = onTelephoneNumberChange,
                    label = { Text(text = "Telephone") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Phone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    prefix = { Text(text = "+44") },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    )
                )

                Text(
                    text = errorMessage ?: "",
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.End
                )
            }

            Button(
                onClick = onSendAction,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "Send".uppercase(),
                    modifier = Modifier.padding(start = 8.dp)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    SignalGSMTheme {
        HomeScreen(
            uiState = HomeUiState(),
            onEvent = { },
            hasPermission = { true },
            requestPermission = { }
        )
    }
}