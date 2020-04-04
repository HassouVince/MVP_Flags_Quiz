package com.systemathic.flagsquizz.ui.activity.form

import android.content.Context
import android.content.Intent
import com.systemathic.flagsquizz.base.BasePresenter
import com.systemathic.flagsquizz.base.BaseView

interface FormContract {

    interface View : BaseView{
        fun onQuit()
        fun onValidInfos(name : String, age : String, pathImage : String)
        fun onErrorInfos(idView : Int, stringRessourceMessage : Int)
        fun onUpdateImage()
        fun onRemoveImage()
        fun onSelectImageFromGallery()
    }

    interface Presenter : BasePresenter {
        fun onValidButtonPressed(name : String, age : String, pathImage : String)
        fun onQuitPressed()
        fun updateImage()
        fun removeImage()
        fun selectImageFromGallery()
        fun getPicturePath(context: Context, data : Intent?) : String
    }
}