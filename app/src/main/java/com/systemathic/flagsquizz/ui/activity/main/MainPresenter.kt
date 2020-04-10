package com.systemathic.flagsquizz.ui.activity.main

import android.content.Intent
import com.systemathic.flagsquizz.app_repository.UserRepositoryImp
import com.systemathic.flagsquizz.model.User
import com.systemathic.flagsquizz.model.question.QuestionManagerImp
import com.systemathic.flagsquizz.model.question_pack.QuestionPackManagerImp
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.lang.Exception

class MainPresenter (private var view: MainContract.View?)
    : KoinComponent, MainContract.Presenter {

    override fun getUserFromRepository(intent: Intent?) = userRepos.getCurrentUser(intent)

    private val userRepos : UserRepositoryImp by inject()
    private val packManager : QuestionPackManagerImp by inject()
    private val questionManager : QuestionManagerImp by inject()

    private lateinit var currentUser : User

    private val REQUEST_FORM_TO_QUIZZ = 1
    private val REQUEST_FORM_TO_PARAMS = 2

    override fun onViewCreated(intent : Intent?) {
        currentUser = getUserFromRepository(intent)
        this.view!!.initView()
    }

    override fun onViewDestroyed() {
        this.view = null
    }

    override fun getUser() = currentUser
    override fun save() = userRepos.saveUser(currentUser)

    override fun onQuizzButtonPressed() {

        fun checkBeforeQuizz() = when {
            currentUser.isRegistred() -> this.view!!.goToQuizz(currentUser)
            else -> this.view!!.goToForm(REQUEST_FORM_TO_QUIZZ)
        }

        if(currentUser.isAllPlayed(packManager,questionManager)){
            this.view!!.onAllQuizzPlayed()
            return
        }
        checkBeforeQuizz()
    }

    override fun onScoreButtonPressed() {
        this.view!!.displayScore()
    }

    override fun onParamsButtonPressed() =
        when {  currentUser.isRegistred() -> this.view!!.goToParams(currentUser)
            else -> this.view!!.goToForm(REQUEST_FORM_TO_PARAMS)}


    override fun onQuitButtonPressed() {
        this.view!!.quit()
    }

    override fun onContactButtonPressed() {
        this.view!!.contact()
    }

    override fun onUserCreated(data: Intent, requestCode : Int) {
        updateCurrentUser(data)
        save()
        if(requestCode == REQUEST_FORM_TO_QUIZZ)
            this.view!!.goToQuizz(currentUser)
        else if(requestCode == REQUEST_FORM_TO_PARAMS){
            this.view!!.goToParams(currentUser)
        }
    }

    private fun updateCurrentUser(data: Intent){
        currentUser.pathImage = data.getStringExtra("img")!!
        currentUser.name = data.getStringExtra("name")!!
        currentUser.age = data.getStringExtra("age")!!.toInt()
    }

    override fun onReset() {
        currentUser.idsQPUsed.clear()
        currentUser.score = 0
        save()
        this.view!!.goToQuizz(currentUser)
    }
}