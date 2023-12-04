package com.msharialsayari.requestpermissionlib.component

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.msharialsayari.requestpermissionlib.findActivity
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
    val shouldShowRationalDialog = rationalDialogParams != null
    val shouldShowDeniedDialog = deniedDialogParams != null
    var openRationaleDialog by rememberSaveable { mutableStateOf(false) }
    var openDeniedDialog by rememberSaveable { mutableStateOf(false) }
    var firstTime by rememberSaveable { mutableStateOf(true) }

    val multiplePermissionState = rememberMultiplePermissionsState(
        permissions = permissions,
        onPermissionsResult = {
            val permissionPermanentlyDenied = it.filter {
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    context.findActivity(),
                    it.key
                ) && !it.value
            }.isNotEmpty()

            when{
                permissionPermanentlyDenied  && shouldShowDeniedDialog ->  openDeniedDialog = true
                else -> onDone()
            }

        }
    )

    if (openRationaleDialog) {
        ShowDialogForMultiplePermission(
            dialogParams = rationalDialogParams!!,
            onConfirmButtonClicked = {
                openRationaleDialog = false
                firstTime = false
                multiplePermissionState.launchMultiplePermissionRequest()
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
                context.findActivity()
                    .startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
                openDeniedDialog = false
                onDone()
            },
            onDismiss = {
                openDeniedDialog = false
                onDone()
            }
        )
    }

    if (firstTime) {
        firstTime = false

        if (multiplePermissionState.allPermissionsGranted) {
            isGranted()
            onDone()
        } else {
            LaunchedEffect(key1 = Unit) {
                if(shouldShowRationalDialog){
                    openRationaleDialog = true
                }else{
                    multiplePermissionState.launchMultiplePermissionRequest()
                }
            }
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