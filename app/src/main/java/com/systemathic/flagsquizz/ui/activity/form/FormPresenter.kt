package com.systemathic.flagsquizz.ui.activity.form

import android.content.Context
import android.content.Intent
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.app_repository.UserRepositoryImp
import com.systemathic.flagsquizz.model.User
import com.systemathic.flagsquizz.utils.getPath
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class FormPresenter (private var view: FormContract.View?)
    : KoinComponent, FormContract.Presenter {

    private val userRepos : UserRepositoryImp by inject()
    private lateinit var currentUser : User

    override fun getUserFromRepository(intent: Intent?): User = userRepos.getCurrentUser(intent)

    override fun onViewCreated(intent: Intent?) {
        this.view?.initView()
    }

    override fun onViewDestroyed() {
        this.view = null
    }

    override fun save() = userRepos.saveUser(currentUser)

    override fun onValidButtonPressed(name : String, age : String, pathImage : String) {

        when{
            name.isEmpty() -> this.view!!.onErrorInfos(R.id.edTextNameForm,R.string.text_requiered)
            name.length<2 -> this.view!!.onErrorInfos(R.id.edTextNameForm,R.string.min_char_edit_text)
            age.isEmpty() -> this.view!!.onErrorInfos(R.id.edTextAgeForm,R.string.text_requiered)
            else -> this.view?.onValidInfos(name, age, pathImage)

        }
    }

    override fun onQuitPressed() {
        this.view?.onQuit()
    }

    override fun updateImage() {
        this.view?.onUpdateImage()
    }

    override fun removeImage() {
        this.view?.onRemoveImage()
    }

    override fun selectImageFromGallery() {
        this.view?.onSelectImageFromGallery()
    }

    override fun getPicturePath(context: Context, data : Intent?): String {
        val selectedImageUri = data?.data
        return getPath(context, selectedImageUri)
    }
}