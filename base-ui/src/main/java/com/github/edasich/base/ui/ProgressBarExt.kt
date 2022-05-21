package com.github.edasich.base.ui

import android.R
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.TypedValue

internal fun getProgressBarDrawable(context: Context): Drawable {
    val value = TypedValue()
    context.theme.resolveAttribute(R.attr.progressBarStyleSmall, value, false)
    val progressBarStyle = value.data
    val attributes = intArrayOf(R.attr.indeterminateDrawable)
    val typedArray: TypedArray = context.obtainStyledAttributes(progressBarStyle, attributes)
    val drawable: Drawable = typedArray.getDrawable(0)!!
    typedArray.recycle()
    return drawable
}