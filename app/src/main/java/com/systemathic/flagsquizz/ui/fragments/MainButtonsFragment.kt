package com.systemathic.flagsquizz.ui.fragments

import android.view.View
import android.widget.Button
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_buttons.*

class MainButtonsFragment : BaseFragment() {

    lateinit var buttons : List<Button>

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseFragment> newInstance(): T = MainButtonsFragment() as T

    override fun getLayoutRessource(): Int = R.layout.fragment_buttons
    override fun initViews(view: View) {}
    override fun setViews(view: View) {
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        buttons = listOf<Button>(button1,button2,button3,button4)
        callback.onFragmentCreated()
    }

    fun setButtonsText(b1Txt : String? = null, b2Txt : String? = null,
                       b3Txt : String? = null,b4Txt : String? = null){
        b1Txt.let { button1.text = b1Txt }
        b2Txt.let { button2.text = b2Txt }
        b3Txt.let { button3.text = b3Txt }
        b4Txt.let { button4.text = b4Txt }
    }

    fun setButtonsText(texts : List<String>){
        for((indice,button) in buttons.withIndex()){
            button.text = texts[indice]
        }
    }

    fun setButtonVisibility(button: Button, visibility : Int){ button.visibility = visibility }
    fun setButtonBackGround(drawableRes : Int, position : Int) = buttons[position].setBackgroundResource(drawableRes)
    fun setAllButtonsBackGround(drawableRes : Int){ for(button in buttons){ button.setBackgroundResource(drawableRes) } }
    fun enableOrDisableAllButtons(enabled : Boolean){ for(button in buttons){ button.isEnabled = enabled } }
}