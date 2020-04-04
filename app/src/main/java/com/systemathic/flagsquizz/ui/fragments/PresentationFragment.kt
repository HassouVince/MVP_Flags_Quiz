package com.systemathic.flagsquizz.ui.fragments

import android.content.Context
import android.view.View
import com.squareup.picasso.Picasso
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.base.BaseFragment
import com.systemathic.flagsquizz.utils.displayImageFromPath
import kotlinx.android.synthetic.main.fragment_presentation.*
import java.lang.ClassCastException
import java.lang.Exception

class PresentationFragment : BaseFragment() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseFragment> newInstance(): T = PresentationFragment() as T

    override fun getLayoutRessource(): Int = R.layout.fragment_presentation
    override fun initViews(view: View) {}
    override fun setViews(view: View) {
        callback.onFragmentCreated()
    }

    fun setImage(res : Int){
        imagePresentation.visibility = View.VISIBLE
        imagePresentation.setImageResource(res)
    }

    fun setImage(path : String?) = if(path == null || path.isEmpty())
        setImage(R.drawable.logo)
        else
        displayImageFromPath(imagePresentation,path)


    interface ImageLoadCallback{
        fun onImageLoadSuccess()
        fun onImageLoadError(e : Exception?)
    }

    lateinit var imageCallback : ImageLoadCallback

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initImageLoadCallBack()
    }
    private fun initImageLoadCallBack()= try {imageCallback = activity as ImageLoadCallback
    }catch (exception : ClassCastException){
        exception.printStackTrace()
    }

    fun setImageWithUrl(url : String){
        Picasso.get()
            .load(url)
            .into(imagePresentation, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    imageCallback.onImageLoadSuccess()
                }
                override fun onError(e: Exception?) {
                    imageCallback.onImageLoadError(e)
                }

            })
    }

    fun setTitle(title : String){
       tvPresentation.text = title
    }

    fun setText(txt : String){
        if(tvInfosPresentation == null)
            return

       tvInfosPresentation.visibility = View.VISIBLE
       tvInfosPresentation.text = txt
    }
}