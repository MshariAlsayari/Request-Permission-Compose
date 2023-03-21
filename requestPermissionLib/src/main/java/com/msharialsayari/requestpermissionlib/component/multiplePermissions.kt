package com.msharialsayari.requestpermissionlib.component

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.msharialsayari.requestpermissionlib.model.DialogParams

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MultiplePermissions(
    permissions: List<String>,
    rationalDialogParams: DialogParams? = null,
    deniedDialogParams: DialogParams? = null,
    isGranted: () -> Unit,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    var openRationaleDialog by remember {
        mutableStateOf(false)
    }

    var openDeniedDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val multiplePermissionState = rememberMultiplePermissionsState(
        permissions = permissions,
        onPermissionsResult = {}
    )

    if (openRationaleDialog) {
        ShowDialogForMultiplePermission(
            dialogParams = rationalDialogParams!!,
            onConfirmButtonClicked = {
                multiplePermissionState.launchMultiplePermissionRequest()
                openRationaleDialog = false
            },
            onDismiss = {
                openRationaleDialog = false
                onDone()
            }
        )
    }

    if (openDeniedDialog) {
        ShowDialogForMultiplePermission(
            dialogParams = deniedDialogParams!!,
            onConfirmButtonClicked = {
                context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                })
                openDeniedDialog = false
            },
            onDismiss = {
                openDeniedDialog = false
                onDone()
            }
        )
    }

    LaunchedEffect(
        multiplePermissionState.allPermissionsGranted,
        multiplePermissionState.shouldShowRationale
    ) {
        if (multiplePermissionState.allPermissionsGranted) {
            isGranted()
            onDone()
        } else if (multiplePermissionState.shouldShowRationale && rationalDialogParams != null) {
            openRationaleDialog = true
        } else if (rationalDialogParams == null) {
            multiplePermissionState.launchMultiplePermissionRequest()
        } else if (!multiplePermissionState.allPermissionsGranted && !multiplePermissionState.shouldShowRationale && deniedDialogParams != null) {
            openDeniedDialog = true
        }
    }
}


@Composable
private fun ShowDialogForMultiplePermission(
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
        onConfirmButtonClicked = onConfirmButtonClicked,
        onDismissButtonClicked = onDismiss
    )
}