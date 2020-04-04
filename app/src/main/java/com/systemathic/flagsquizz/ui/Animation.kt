package com.systemathic.flagsquizz.ui

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.animation.AlphaAnimation


fun alphaViewAnimation(view: View, startDelay: Int, duration : Long) {
    val animation = AlphaAnimation(0.0f, 1.0f)
    animation.setDuration(duration)
    animation.setStartOffset(startDelay.toLong())
    view.startAnimation(animation)
}

fun fromTopAnimation(view: View, startDelay: Int, duration : Long) {
    val animation = TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, -1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f
    )
    animation.setDuration(duration)
    animation.setInterpolator(AccelerateInterpolator())
    animation.setStartOffset(startDelay.toLong())
    view.startAnimation(animation)
}