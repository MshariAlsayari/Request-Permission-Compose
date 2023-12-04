package com.msharialsayari.requestpermissionlib.component

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.msharialsayari.requestpermissionlib.findActivity
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
    val context = LocalContext.current
    val shouldShowRationalDialog = rationalDialogParams != null
    val shouldShowDeniedDialog = deniedDialogParams != null
    var openRationaleDialog by rememberSaveable { mutableStateOf(false) }
    var openDeniedDialog by rememberSaveable { mutableStateOf(false) }
    var firstTime by rememberSaveable { mutableStateOf(true) }

    val permissionState = rememberPermissionState(permission = permission, onPermissionResult = { isGranted ->
        val permissionPermanentlyDenied = !ActivityCompat.shouldShowRequestPermissionRationale(context.findActivity(), permission) && !isGranted
        when{
            permissionPermanentlyDenied  && shouldShowDeniedDialog ->  openDeniedDialog = true
            else -> onDone()
        }
    })

    if (openRationaleDialog) {
        ShowDialog(rationalDialogParams!!,
            onConfirmButtonClicked = {
                openRationaleDialog = false
                firstTime = false
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
                context.findActivity().startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                })
                openDeniedDialog = false
                onDone()
            },
            onDismiss = {
                openDeniedDialog = false
                onDone()
            })
    }

    if (firstTime) {
        firstTime = false
        if (permissionState.status.isGranted) {
            isGranted()
            onDone()
        } else {
            LaunchedEffect(key1 = Unit) {
                if(shouldShowRationalDialog){
                    openRationaleDialog = true
                }else{
                    permissionState.launchPermissionRequest()
                }
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
        onDismissButtonClicked = {
            onDismiss()
        }
    )
}