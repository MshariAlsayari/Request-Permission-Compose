package com.msharialsayari.requestpermissionlib.component

import androidx.compose.runtime.*
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
