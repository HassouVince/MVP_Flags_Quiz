package com.systemathic.flagsquizz.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.ClassCastException

abstract class BaseFragment : Fragment(){

    abstract fun<T : BaseFragment> newInstance() : T
    abstract fun getLayoutRessource() : Int
    abstract fun initViews(view: View)
    abstract fun setViews(view: View)

    interface OnViewClickListener {
        fun onViewClick(v: View?)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutRessource(), container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(view)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initOnViewCreatedCallback()
    }

    interface CallbackOnFragmentCreated{
        fun onFragmentCreated()
    }

    lateinit var callback : CallbackOnFragmentCreated

    private fun initOnViewCreatedCallback()= try {callback = activity as CallbackOnFragmentCreated
    }catch (exception : ClassCastException){exception.printStackTrace()}
}


abstract class ClickableFragment : BaseFragment(), View.OnClickListener {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initBtnListener()
    }

    lateinit var callbackBtn : OnViewClickListener

    override fun onClick(v: View?) = callbackBtn.onViewClick(v)

    private fun initBtnListener()= try {callbackBtn = activity as OnViewClickListener
    }catch (exception : ClassCastException){
        throw ClassCastException("Interface ButtonClickListener must be implemented on activity (Exception : $exception )")
    }
}
