package com.systemathic.flagsquizz.ui.activity.params

import com.systemathic.flagsquizz.base.BasePresenter
import com.systemathic.flagsquizz.base.BaseView
import com.systemathic.flagsquizz.model.User

interface ParamsContract {

    interface View : BaseView{
        fun displayUserInfos()
        fun displayEditText(visibility : Int, arg: String)
        fun onUpdate()
        fun onUpdateError()
        fun openGallery()
        fun onRemoveUserImage()
        fun showResetMessage()
        fun onReset()
        fun showQuitMessage()
        fun onQuit()
        fun showHomeMessage()
        fun onBackToHome()
        fun onContact()
        fun onShare()
    }

    interface Presenter : BasePresenter {
        fun onValidButtonPressed(arg : String, txt : String)
        fun onDialButtonPressed(dialChoice : String)
        fun onNamePressed()
        fun onAgePressed()
        fun onUpdateUserImagePressed()
        fun onImageSelected(path : String)
        fun onRemoveUserImagePressed()
        fun onResetPressed()
        fun onQuitPressed()
        fun onBackToHomePressed()
        fun onContactPressed()
        fun onSharePressed()
        fun getCurrentUser() : User
        fun initUser()
    }
}