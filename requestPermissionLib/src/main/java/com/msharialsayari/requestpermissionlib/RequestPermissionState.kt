package com.msharialsayari.requestpermissionlib

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class RequestPermissionState(initRequest: Boolean, val permission: String) {
    var requestPermission by mutableStateOf(initRequest)
}