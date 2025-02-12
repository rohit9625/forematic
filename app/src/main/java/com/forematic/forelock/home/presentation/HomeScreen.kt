package com.forematic.forelock.home.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.forematic.forelock.R
import com.forematic.forelock.Route
import com.forematic.forelock.home.presentation.components.ExpandableFAB
import com.forematic.forelock.home.presentation.components.SwitchWithText
import com.forematic.forelock.setupdevice.presentation.DeviceType
import com.forematic.forelock.setupdevice.presentation.OutputRelayText
import com.forematic.forelock.ui.theme.ForeLockTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    hasPermission: ()-> Boolean,
    requestPermission: () -> Unit,
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var isFabExpanded by remember { mutableStateOf(false) }
    val menuItems by remember { mutableStateOf(listOf(
            "R21 Relay" to { /* Navigate to R21 Relay setup */ },
            "G64 Relay" to { /* Navigate to G64 Relay setup */ },
            "G24 Intercom" to { navController.navigate(Route.G24SetupScreen) }
        ))
    }

    var isBigGateOpen by remember { mutableStateOf(false) }
    var isOfficeGateOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "by Forematic",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExpandableFAB(
                isExpanded = isFabExpanded,
                menuItems = menuItems,
                onClick = { isFabExpanded = !isFabExpanded }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GateDeviceControls(
                gateName = "Office Gate",
                isGateOpen = isOfficeGateOpen,
                gateIcon = R.drawable.ic_office_gate,
                deviceType = DeviceType.G24_INTERCOM,
                statusText = OutputRelayText.UNLOCK_LOCK,
                onStatusChange = { isOfficeGateOpen = it },
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            GateDeviceControls(
                gateName = "Big Gate",
                isGateOpen = isBigGateOpen,
                gateIcon = R.drawable.ic_big_gate_brown,
                deviceType = DeviceType.G24_INTERCOM,
                statusText = OutputRelayText.OPEN_CLOSE,
                onStatusChange = { isBigGateOpen = it },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
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

@Composable
fun GateDeviceControls(
    gateName: String,
    isGateOpen: Boolean,
    @DrawableRes gateIcon: Int,
    deviceType: DeviceType,
    statusText: OutputRelayText,
    onStatusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val splitStatusText = remember(statusText) { splitStatusText(statusText.displayName) }
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            ElevatedCard(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Icon(
                    painter = painterResource(gateIcon),
                    contentDescription = gateName,
                    modifier = Modifier.padding(8.dp).size(40.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = gateName,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = deviceType.displayName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            SwitchWithText(
                isChecked = isGateOpen,
                onCheckedChange = onStatusChange,
                textOnOFF = splitStatusText
            )

            ElevatedButton(
                onClick = { },
                contentPadding = PaddingValues(),
                shape = CircleShape,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null
                )
            }
        }
    }
}

/**
 * Splits the input string into two parts based on the "/" delimiter.
 *
 * @param text The input string to be split, expected to contain a single "/".
 *             For example: "Open/Close".
 * @return A Pair where the first element is the substring before the "/",
 *         and the second element is the substring after the "/".
 */
fun splitStatusText(text: String): Pair<String, String> {
    val first = text.substringBefore('/')
    val second = text.substringAfter('/')
    return Pair(first, second)
}


@Preview
@Composable
private fun DeviceItemPreview() {
    ForeLockTheme {
        Surface {
            GateDeviceControls(
                gateName = "Office Gate",
                isGateOpen = false,
                gateIcon = R.drawable.ic_office_gate_brown,
                deviceType = DeviceType.G24_INTERCOM,
                statusText = OutputRelayText.OPEN_CLOSE,
                onStatusChange = { },
                modifier = Modifier.padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    ForeLockTheme {
        HomeScreen(
            uiState = HomeUiState(),
            onEvent = { },
            hasPermission = { true },
            requestPermission = { },
            navController = rememberNavController()
        )
    }
}