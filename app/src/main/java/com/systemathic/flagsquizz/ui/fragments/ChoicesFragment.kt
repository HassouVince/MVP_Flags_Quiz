package com.systemathic.flagsquizz.ui.fragments

import android.view.View
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_choices.view.*

class ChoicesFragment : BaseFragment(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseFragment> newInstance(): T = ChoicesFragment() as T

    override fun getLayoutRessource(): Int = R.layout.fragment_choices
    override fun initViews(view: View) {
        view.imgChoicesReturn.setOnClickListener(this)
        view.imgChoicesValid.setOnClickListener(this)
    }
    override fun setViews(view: View) {}
}