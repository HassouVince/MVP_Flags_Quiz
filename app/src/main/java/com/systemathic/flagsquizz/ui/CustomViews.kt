package com.systemathic.flagsquizz.ui

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.systemathic.flagsquizz.utils.MAIN_TYPEFONT

class CustomTextView(context: Context?, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    init {
        val tf = (Typeface.createFromAsset(context!!.assets, MAIN_TYPEFONT))
        typeface = tf
    }
}