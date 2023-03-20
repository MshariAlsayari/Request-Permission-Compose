package com.msharialsayari.requestpermissionlib.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class DialogParams (
    @StringRes val title:Int,
    @StringRes val message:Int,
    @StringRes val positiveButtonText:Int =  android.R.string.ok,
    @StringRes val negativeButtonText:Int =  android.R.string.cancel,
    @DrawableRes val icon:Int? = null,
    val isCancelable :Boolean = false,
)