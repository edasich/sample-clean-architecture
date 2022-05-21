package com.github.edasich.base.ui

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Animatable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.showProgress(
    showProgress: Boolean,
    @DrawableRes
    iconSource: Int,
) {
    if (showProgress) {
        imageTintMode = PorterDuff.Mode.MULTIPLY
        imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.secondaryColor))

        val drawable = getProgressBarDrawable(context = context)
        setImageDrawable(drawable)
        (drawable as Animatable).start()
    } else {
        imageTintMode = PorterDuff.Mode.MULTIPLY
        imageTintList = null

        setImageResource(iconSource)
    }
}