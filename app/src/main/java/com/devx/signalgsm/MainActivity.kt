package com.devx.signalgsm

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
import com.devx.signalgsm.ui.components.MessagePermissionText
import com.devx.signalgsm.ui.components.PermissionRationale
import com.devx.signalgsm.ui.screens.HomeScreen
import com.devx.signalgsm.ui.screens.HomeViewModel
import com.devx.signalgsm.ui.theme.SignalGSMTheme

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
            SignalGSMTheme {
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
                    }
                )

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