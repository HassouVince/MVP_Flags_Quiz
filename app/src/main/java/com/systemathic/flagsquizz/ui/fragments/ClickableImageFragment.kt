package com.systemathic.flagsquizz.ui.fragments

import android.view.View
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.base.BaseFragment
import com.systemathic.flagsquizz.base.ClickableFragment
import kotlinx.android.synthetic.main.fragment_clickable_image.*

class ClickableImageFragment : ClickableFragment() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseFragment> newInstance(): T = ClickableImageFragment() as T

    override fun getLayoutRessource(): Int = R.layout.fragment_clickable_image
    override fun initViews(view: View) {}
    override fun setViews(view: View) {
        imageClickableImage.setOnClickListener(this)
        callback.onFragmentCreated()
    }

    fun setImage(resImage : Int){
        imageClickableImage.setImageResource(resImage)
    }

}