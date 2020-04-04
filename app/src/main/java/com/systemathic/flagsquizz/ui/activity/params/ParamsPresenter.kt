package com.systemathic.flagsquizz.ui.activity.params

import android.content.Intent
import android.view.View
import com.systemathic.flagsquizz.app_repository.UserRepositoryImp
import com.systemathic.flagsquizz.model.User
import com.systemathic.flagsquizz.utils.ParamsKeys
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class ParamsPresenter (private var view: ParamsContract.View?)
    : KoinComponent, ParamsContract.Presenter {

    override fun getUserFromRepository(intent: Intent?): User = userRepos.getCurrentUser(intent)

    private val userRepos : UserRepositoryImp by inject()

    private lateinit var currentUser : User

    override fun onViewCreated(intent: Intent?) {
        currentUser = getUserFromRepository(intent)
        this.view!!.initView()
    }

    override fun onViewDestroyed() {
        this.view = null
    }

    override fun initUser(){
        currentUser = User()
        save()
    }
    override fun getCurrentUser() = currentUser
    override fun save() = userRepos.saveUser(currentUser)

    override fun onValidButtonPressed(arg : String, txt : String) {
        if(updateBasicUserAttributes(arg,txt)) {
            save()
            this.view!!.onUpdate()
        }
    }

    private fun updateBasicUserAttributes(arg : String, txt : String) : Boolean{
        if(arg == ParamsKeys.AGE_KEY.key){
            if(txt.isEmpty()){ this.view!!.onUpdateError(); return false}
            currentUser.age = txt.toInt()
        }else if(arg == ParamsKeys.NAME_KEY.key){
            if(txt.length<2){ this.view!!.onUpdateError(); return false}
            currentUser.name = txt
        }
        return true
    }

    override fun onDialButtonPressed(dialChoice: String) {

        dialChoice.let {

            save()

            fun quit() = this.view!!.onQuit()
            fun backToHome() =this.view!!.onBackToHome()
            fun reset() {initUser(); this.view!!.onReset()}

            when(it){
                ParamsKeys.DIAL_KEY_RESET.key -> reset()
                ParamsKeys.DIAL_KEY_HOME.key -> backToHome()
                ParamsKeys.DIAL_KEY_QUIT.key -> quit()
            }
        }
    }

    override fun onNamePressed() = this.view!!.displayEditText(View.VISIBLE, ParamsKeys.NAME_KEY.key)
    override fun onAgePressed() = this.view!!.displayEditText(View.VISIBLE, ParamsKeys.AGE_KEY.key)

    override fun onUpdateUserImagePressed() {
        this.view!!.openGallery()
    }

    override fun onImageSelected(path: String) {
        currentUser.pathImage = path
        save()
    }

    override fun onRemoveUserImagePressed() {
        if(currentUser.pathImage.isNotEmpty()){
            currentUser.pathImage = ""
            save()
            this.view!!.onRemoveUserImage()
        }
    }

    override fun onResetPressed() = this.view!!.showResetMessage()
    override fun onQuitPressed() = this.view!!.showQuitMessage()
    override fun onBackToHomePressed() = this.view!!.showHomeMessage()

    override fun onSharePressed() {
        this.view!!.onShare()
    }

    override fun onContactPressed() {
        this.view!!.onContact()
    }
}