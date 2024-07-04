package com.msharialsayari.requestpermissionlib.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.msharialsayari.requestpermissionlib.model.DialogParams
import com.msharialsayari.requestpermissionlib.model.TextStyleParams
import com.msharialsayari.requestpermissionlib.model.getMessage
import com.msharialsayari.requestpermissionlib.model.getTitle


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyAlertDialog(
    dialogParams: DialogParams,
    onConfirmButtonClicked: (() -> Unit)? = null,
    onDismissButtonClicked: (() -> Unit)? = null,

    ) {

    val confirmButtonText = stringResource(id = dialogParams.positiveButtonText)
    val dismissButtonText = stringResource(id = dialogParams.negativeButtonText)
    val title = dialogParams.getTitle()
    val message = dialogParams.getMessage()
    val icon = dialogParams.icon
    val isCancelable = dialogParams.isCancelable
    val titleTextStyle = dialogParams.titleTextStyle


    val confirmButtons: @Composable () -> Unit = {
        if (confirmButtonText.isNotEmpty()) {
            TextButton(onClick = {
                onConfirmButtonClicked?.invoke()
            }) {
                DialogButtonText(confirmButtonText, dialogParams.positiveButtonTextStyle)
            }
        }
    }

    val dismissButton: @Composable () -> Unit = {
        if (dismissButtonText.isNotEmpty()) {
            TextButton(onClick = {
                onDismissButtonClicked?.invoke()
            }) {
                DialogButtonText(dismissButtonText, dialogParams.positiveButtonTextStyle)
            }
        }
    }

    val dialogTitle: (@Composable () -> Unit)? = title?.let {
        @Composable {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                icon?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = icon),
                        tint = dialogParams.iconStyle.color,
                        modifier = Modifier.size(dialogParams.iconStyle.size),
                        contentDescription = null,
                    )
                }

                Text(
                    text = it,
                    style = TextStyle(
                        color = titleTextStyle.color,
                        fontWeight = titleTextStyle.fontWeight ?: FontWeight.Bold,
                        fontSize = titleTextStyle.fontSize,
                        fontFamily = titleTextStyle.fontFamily,
                        fontStyle = titleTextStyle.fontStyle,
                        textAlign = titleTextStyle.textAlign
                    ),
                )
            }
        }
    }


    AlertDialog(
        onDismissRequest = { onDismissButtonClicked?.invoke() },
        confirmButton = { confirmButtons.invoke() },
        dismissButton = { dismissButton.invoke() },
        title = { dialogTitle?.invoke() },
        text = { DialogMessage(value = message, style = dialogParams.messageTextStyle) },
        properties = DialogProperties(
            dismissOnBackPress = isCancelable,
            dismissOnClickOutside = isCancelable,
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
private fun DialogMessage(value: String, style: TextStyleParams) {
    Text(
        text = value,
        style = TextStyle(
            color = style.color,
            fontWeight = style.fontWeight,
            fontSize = style.fontSize,
            fontFamily = style.fontFamily,
            fontStyle = style.fontStyle,
            textAlign = style.textAlign
        ),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun DialogButtonText(value: String, style: TextStyleParams) {
    Text(
        text = value,
        style = TextStyle(
            color = style.color,
            fontWeight = style.fontWeight,
            fontSize = style.fontSize,
            fontFamily = style.fontFamily,
            fontStyle = style.fontStyle,
            textAlign = style.textAlign
        ),
    )
}

