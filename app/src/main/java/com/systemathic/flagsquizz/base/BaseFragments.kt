package com.systemathic.flagsquizz.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.ClassCastException

abstract class BaseFragment : Fragment(), View.OnClickListener{

    abstract fun<T : BaseFragment> newInstance() : T
    abstract fun getLayoutRessource() : Int
    abstract fun initViews(view: View)
    abstract fun setViews(view: View)

    interface Callback{
        fun onFragmentCreated()
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
        initCallback()
    }

    lateinit var callback : Callback

    override fun onClick(v: View?) = callback.onViewClick(v)

    private fun initCallback()= try {callback = activity as Callback
    }catch (exception : ClassCastException){exception.printStackTrace()}
}

