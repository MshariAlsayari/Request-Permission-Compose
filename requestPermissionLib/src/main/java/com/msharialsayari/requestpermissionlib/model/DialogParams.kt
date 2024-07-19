package com.msharialsayari.requestpermissionlib.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource

data class DialogParams (
    val modifier: Modifier? = null,
    @StringRes val title:Int? = null,
    @StringRes val message:Int? = null,
    val titleStr:String? = null,
    val messageStr:String = "",
    @StringRes val positiveButtonText:Int =  android.R.string.ok,
    @StringRes val negativeButtonText:Int =  android.R.string.cancel,
    @DrawableRes val icon:Int? = null,
    val titleTextStyle:TextStyleParams = TextStyleParams(),
    val messageTextStyle:TextStyleParams = TextStyleParams(),
    val positiveButtonTextStyle:TextStyleParams = TextStyleParams(),
    val negativeButtonTextStyle:TextStyleParams = TextStyleParams(),
    val iconStyle:IconStyleParams = IconStyleParams(),
    val shape: Shape? = null,
    val backgroundColor: Color? = null,
    val isCancelable :Boolean = false
)

@Composable
fun DialogParams.getTitle()= if(title !=  null) stringResource(id = title) else titleStr

@Composable
fun DialogParams.getMessage()= if(message !=  null) stringResource(id = message) else messageStr