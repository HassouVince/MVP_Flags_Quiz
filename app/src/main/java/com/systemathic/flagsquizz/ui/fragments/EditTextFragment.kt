package com.systemathic.flagsquizz.ui.fragments

import android.text.InputFilter
import android.view.View
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.base.BaseFragment
import com.systemathic.flagsquizz.base.ClickableFragment
import kotlinx.android.synthetic.main.fragment_edit_text.*


class EditTextFragment : ClickableFragment(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseFragment> newInstance(): T = EditTextFragment() as T

    override fun getLayoutRessource(): Int = R.layout.fragment_edit_text
    override fun initViews(view: View) {
        choicesFragment = ChoicesFragment().newInstance()
        addChoicesFragment()
    }
    override fun setViews(view: View) {}

    lateinit var choicesFragment: BaseFragment

    // FRAG CHOICE IN THIS FRAG

    fun addChoicesFragment(){
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layoutFragEditParamsChoices, choicesFragment)
        fragmentTransaction.commit()
    }

    fun setEditText(visibility : Int? = null, txt : String? = null,
                    hint : String? = null, inputType : Int? = null,
                    errorMsg : String? = null, maxLength : Int? = null){

        fun setText(txt : String?){txt.let {edTextParams.setText(txt)}}
        fun setHint(txt : String?){txt.let {edTextParams.hint = txt}}
        fun setError(msg : String?){msg.let {edTextParams.error = msg}}
        fun setVisibility(visibility : Int?) = if(visibility!=null) edTextParams.visibility = visibility else{}
        fun setInputType(type : Int?) = if (type != null) edTextParams.inputType = type else{}
        fun setMaxLength(maxLength : Int?){
            if(maxLength!=null) {
                val filterArray = arrayOfNulls<InputFilter>(1)
                filterArray[0] = InputFilter.LengthFilter(maxLength)
                edTextParams.filters = filterArray
            }
        }

        setVisibility(visibility)
        setText(txt)
        setHint(hint)
        setInputType(inputType)
        setError(errorMsg)
        setMaxLength(maxLength)
    }

    fun setBtnsLayoutVisibility(visibility : Int){
        layoutFragEditParamsChoices.visibility = visibility
    }

    fun getEditTextInput() = edTextParams.text.toString()
}