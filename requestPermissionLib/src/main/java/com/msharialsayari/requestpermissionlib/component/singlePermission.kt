package com.msharialsayari.requestpermissionlib.component


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.*
import com.msharialsayari.requestpermissionlib.model.DialogParams


@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SinglePermission(
    permission: String,
    rationalDialogParams: DialogParams? = null,
    deniedDialogParams: DialogParams? = null,
    isGranted: () -> Unit,
    onDone: () -> Unit,
) {
    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current
    val permissionState = rememberPermissionState(permission = permission, onPermissionResult = {})

    val shouldShowRationalDialog = rationalDialogParams != null
    val shouldShowDeniedDialog = deniedDialogParams != null


    var systemDialogOpened by rememberSaveable {
        mutableStateOf(false)
    }


    var openRationaleDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var openDeniedDialog by rememberSaveable {
        mutableStateOf(false)
    }


    if (openRationaleDialog) {
        ShowDialog(rationalDialogParams!!,
            onConfirmButtonClicked = {
                openRationaleDialog = false
                systemDialogOpened = true
                permissionState.launchPermissionRequest()
            },
            onDismiss = {
                openRationaleDialog = false
                onDone()
            }
        )
    }

    if (openDeniedDialog) {
        ShowDialog(deniedDialogParams!!,
            onConfirmButtonClicked = {
                openDeniedDialog = false
                onDone()
                activity?.let {
                    context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
                }
            },
            onDismiss = {
                openDeniedDialog = false
                onDone()
            })
    }


    LaunchedEffect(key1 = permissionState.status) {
        when {
            permissionState.status.isGranted -> {
                isGranted()
                onDone()
            }

            permissionState.status.shouldShowRationale -> {
                if (shouldShowRationalDialog) {
                    openRationaleDialog = true
                } else {
                    openRationaleDialog = false
                    onDone()
                }

            }

            !permissionState.status.isGranted && !permissionState.status.shouldShowRationale -> {
                if (shouldShowDeniedDialog && !systemDialogOpened) {
                    openDeniedDialog = true
                } else {
                    openDeniedDialog = false
                    onDone()
                }
            }

            else -> {
                onDone()
            }


        }

    }


}

@Composable
fun ShowDialog(
    dialogParams: DialogParams,
    onConfirmButtonClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    MyAlertDialog(
        title = stringResource(dialogParams.title),
        message = stringResource(dialogParams.message),
        confirmButtonText = stringResource(id = dialogParams.positiveButtonText),
        dismissButtonText = stringResource(id = dialogParams.negativeButtonText),
        isCancelable = dialogParams.isCancelable,
        icon = dialogParams.icon,
        onConfirmButtonClicked = {
            onConfirmButtonClicked()
        },
        onDismissButtonClicked = onDismiss
    )
}