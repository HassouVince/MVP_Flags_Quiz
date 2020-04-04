package com.systemathic.flagsquizz.ui.fragments

import android.view.View
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.base.BaseFragment
import com.systemathic.flagsquizz.base.ClickableFragment
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.fragment_form.view.*

class FormFragment : ClickableFragment(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseFragment> newInstance(): T = FormFragment() as T

    override fun getLayoutRessource(): Int = R.layout.fragment_form
    override fun initViews(view: View) {
        view.buttonForm.setOnClickListener(this)
    }
    override fun setViews(view: View) {}


    fun setImage(res : Int){
        imageForm.setImageResource(res)
    }

    fun getImageForm() = imageForm

    fun setButtonText(text : String){
        buttonForm.text = text
    }

}