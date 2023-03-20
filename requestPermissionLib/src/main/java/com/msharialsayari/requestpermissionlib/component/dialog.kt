package com.msharialsayari.requestpermissionlib.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyAlertDialog(
    title: String? = null,
    message: String,
    icon: Int? = null,
    confirmButtonText: String? = null,
    dismissButtonText: String? = null,
    onConfirmButtonClicked: (() -> Unit)? = null,
    onDismissButtonClicked: (() -> Unit)? = null,
    isCancelable: Boolean = true
) {
    val confirmButtons: @Composable () -> Unit = {
        if (!confirmButtonText.isNullOrEmpty()) {
            TextButton(onClick = {
                onConfirmButtonClicked?.invoke()
            }) {
                Text(confirmButtonText)
            }
        }

    }

    val dismissButton: (@Composable () -> Unit)? = dismissButtonText?.let {
        @Composable {
            TextButton(onClick = {
                onDismissButtonClicked?.invoke()
            }) {
                Text(it)
            }
        }
    }

    val titleComposable: (@Composable () -> Unit)? = title?.let {
        @Composable {
            Row(
                modifier = Modifier.fillMaxWidth(),

                ) {

                icon?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = icon),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = it,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                )
            }
        }
    }

    val msg = @Composable {
        Text(
            text = message,
            modifier = Modifier.fillMaxWidth()
        )
    }

    AlertDialog(
        onDismissRequest = { onDismissButtonClicked?.invoke() },
        confirmButton = { confirmButtons.invoke() },
        dismissButton = { dismissButton?.invoke() },
        title = { titleComposable?.invoke() },
        text = { msg() },
        properties = DialogProperties(
            dismissOnBackPress = isCancelable,
            dismissOnClickOutside = isCancelable,
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier.padding(16.dp)
    )
}