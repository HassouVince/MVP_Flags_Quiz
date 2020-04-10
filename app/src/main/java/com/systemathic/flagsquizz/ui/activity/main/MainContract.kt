package com.systemathic.flagsquizz.ui.activity.main

import android.content.Intent
import com.systemathic.flagsquizz.base.BasePresenter
import com.systemathic.flagsquizz.base.BaseView
import com.systemathic.flagsquizz.model.User

interface MainContract {

    interface View : BaseView{
        fun displayScore()
        fun goToParams(user : User)
        fun goToForm(requestCode: Int)
        fun goToQuizz(user : User)
        fun contact()
        fun quit()
        fun onAllQuizzPlayed()
    }
    interface Presenter : BasePresenter {
        fun onUserCreated(data : Intent, requestCode : Int)
        fun onQuizzButtonPressed()
        fun onScoreButtonPressed()
        fun onParamsButtonPressed()
        fun onQuitButtonPressed()
        fun onContactButtonPressed()
        fun onReset()
        fun getUser() : User
    }
}