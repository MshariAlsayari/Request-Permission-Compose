package com.msharialsayari.requestpermissionlib.component

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.msharialsayari.requestpermissionlib.findActivity
import com.msharialsayari.requestpermissionlib.model.DialogParams
import com.msharialsayari.requestpermissionlib.navigateToSetting

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

    val permissionState = rememberPermissionState(permission = permission, onPermissionResult = { isPermissionsGranted ->
        val permissionPermanentlyDenied = !ActivityCompat.shouldShowRequestPermissionRationale(context.findActivity(), permission) && !isPermissionsGranted
        when{
            permissionPermanentlyDenied  && shouldShowDeniedDialog ->  openDeniedDialog = true
            isPermissionsGranted -> {
                isGranted()
                onDone()
            }
            else ->{
                onDone()
            }
        }
    })

    if (openRationaleDialog) {
        ShowDialog(rationalDialogParams!!,
            onConfirmButtonClicked = {
                openRationaleDialog = false
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
                navigateToSetting(context)
                openDeniedDialog = false
                onDone()
            },
            onDismiss = {
                openDeniedDialog = false
                onDone()
            })
    }

    LaunchedEffect(Unit) {
        if (permissionState.status.isGranted) {
            isGranted()
            onDone()
        } else if (shouldShowRationalDialog) {
            openRationaleDialog = true
        } else {
            permissionState.launchPermissionRequest()
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
       dialogParams = dialogParams,
        onConfirmButtonClicked = onConfirmButtonClicked,
        onDismissButtonClicked = onDismiss
    )
}