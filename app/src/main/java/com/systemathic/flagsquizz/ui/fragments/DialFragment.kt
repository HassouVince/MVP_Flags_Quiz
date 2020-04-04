package com.systemathic.flagsquizz.ui.fragments

import android.util.Log
import android.view.View
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.base.BaseFragment
import com.systemathic.flagsquizz.base.ClickableFragment
import kotlinx.android.synthetic.main.fragment_dial.*
import kotlinx.android.synthetic.main.fragment_dial.view.*
import java.lang.Exception

class DialFragment : ClickableFragment(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseFragment> newInstance(): T = DialFragment() as T

    override fun getLayoutRessource(): Int = R.layout.fragment_dial
    override fun initViews(view: View) {
        view.btnDial1.setOnClickListener(this)
        view.btnDial2.setOnClickListener(this)
        view.btnDial3.setOnClickListener(this)
    }
    override fun setViews(view: View) {}

    companion object{
        const val TAG_BTN_DIAL_1 = "btn_dial_1"
        const val TAG_BTN_DIAL_2 = "btn_dial_2"
        const val TAG_BTN_DIAL_3 = "btn_dial_3"
    }

    fun display(title : String? = null, message : String,
                                         resIcon : Int? = null, txtNegative : String,
                                         txtPositive : String? = null, txtNeutral : String? = null){

        try {
            textTitleDial.text = title ?: ""
            textDial.text = message
            btnDial1.text = txtNegative

            fun setView(any: Any?, view: View) = if(any == null) view.visibility = View.GONE
            else view.visibility = View.VISIBLE
            setView(resIcon,imgDial); setView(txtPositive,btnDial2); setView(txtNeutral,btnDial3)

            resIcon.let {imgDial.setImageResource(it!!)}
            txtPositive.let {btnDial2.text = it!!}
            txtNeutral.let {btnDial3.text = it!!}

            //if(txtPositive != null)btnDial2.text = txtPositive

        }catch (e : Exception){
            Log.e("Diall",e.toString())
        }
    }
}
