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












const val TAG = "RequestPermissions"
