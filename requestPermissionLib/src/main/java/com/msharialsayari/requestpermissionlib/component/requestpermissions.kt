package com.msharialsayari.requestpermissionlib.component

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.*
import com.msharialsayari.requestpermissionlib.model.DialogParams

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(
    permissions: List<String>,
    rationalDialogParams: DialogParams? = null,
    deniedDialogParams: DialogParams? = null,
    isGranted: () -> Unit,
    onDone: () -> Unit
){

    when{
        permissions.isEmpty() -> throw  Exception("The list of permissions is Empty")
        permissions.size == 1 -> SinglePermission(
            permission = permissions[0],
            rationalDialogParams=rationalDialogParams,
            deniedDialogParams=deniedDialogParams,
            isGranted=isGranted,
            onDone=onDone
        )
        else -> MultiplePermissions(
            permissions = permissions,
            rationalDialogParams=rationalDialogParams,
            deniedDialogParams=deniedDialogParams,
            isGranted=isGranted,
            onDone=onDone
        )

    }

}




@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MultiplePermissions(
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

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SinglePermission(
    permission: String,
    rationalDialogParams: DialogParams? = null,
    deniedDialogParams: DialogParams? = null,
    isGranted: () -> Unit,
    onDone: () -> Unit,
) {
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
                context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                })
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