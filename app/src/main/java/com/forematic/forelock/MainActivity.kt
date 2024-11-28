package com.forematic.forelock

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.forematic.forelock.ui.components.MessagePermissionText
import com.forematic.forelock.ui.components.PermissionRationale
import com.forematic.forelock.home.presentation.HomeScreen
import com.forematic.forelock.home.presentation.HomeViewModel
import com.forematic.forelock.setupdevice.presentation.SetupDeviceViewModel
import com.forematic.forelock.setupdevice.presentation.SetupNewDeviceScreen
import com.forematic.forelock.ui.theme.ForeLockTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    private var showPermissionRationale by mutableStateOf(false)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted->
        showPermissionRationale = !isGranted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ForeLockTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Route.Home) {
                    composable<Route.Home> {
                        val viewModel = viewModel {
                            HomeViewModel(
                                smsManager = MyApplication.appModule.smsManager
                            )
                        }
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        HomeScreen(
                            uiState = uiState,
                            onEvent = viewModel::onEvent,
                            hasPermission = { hasPermission(Manifest.permission.SEND_SMS) },
                            requestPermission = {
                                requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)
                            },
                            onAddNewDevice = {
                                navController.navigate(Route.SetupDevice)
                            }
                        )
                    }

                    composable<Route.SetupDevice> {
                        val viewModel = viewModel { SetupDeviceViewModel() }
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                        SetupNewDeviceScreen(
                            uiState = uiState,
                            onEvent = viewModel::onEvent,
                            onNavigateBack = { navController.navigateUp() }
                        )
                    }
                }

                if (showPermissionRationale) {
                    PermissionRationale(
                        permissionTextProvider = MessagePermissionText(),
                        isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                            Manifest.permission.SEND_SMS),
                        onConfirm = {
                            showPermissionRationale = false
                            requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)
                        },
                        onDismiss = { showPermissionRationale = false },
                        onGoToAppSettings = this::openAppSettings
                    )
                }
            }
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        ).also { startActivity(it) }
    }
}

sealed interface Route{
    @Serializable
    data object Home: Route

    @Serializable
    data object SetupDevice: Route
}