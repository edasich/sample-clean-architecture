package com.github.edasich.base.ui

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

fun Context.bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
    AppCompatResources.getDrawable(context, resourceId)?.convertDrawableToBitmap()
