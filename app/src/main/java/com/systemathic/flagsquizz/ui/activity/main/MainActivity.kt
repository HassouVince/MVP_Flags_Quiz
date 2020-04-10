package com.systemathic.flagsquizz.ui.activity.main

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import android.view.View
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.base.BaseFragment
import com.systemathic.flagsquizz.base.BasePresenter
import com.systemathic.flagsquizz.model.User
import com.systemathic.flagsquizz.net.isConnectionOk
import com.systemathic.flagsquizz.ui.activity.form.FormActivity
import com.systemathic.flagsquizz.ui.activity.params.ParamsActivity
import com.systemathic.flagsquizz.ui.activity.quizz.QuizzActivity
import com.systemathic.flagsquizz.ui.alphaViewAnimation
import com.systemathic.flagsquizz.ui.fragments.DialFragment
import com.systemathic.flagsquizz.ui.fragments.MainButtonsFragment
import com.systemathic.flagsquizz.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_buttons.*
import kotlinx.android.synthetic.main.fragment_dial.*
import kotlinx.android.synthetic.main.nav_bottom.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity(), MainContract.View, BaseFragment.OnViewClickListener
    , BottomNavigationView.OnNavigationItemSelectedListener, BaseFragment.CallbackOnFragmentCreated {

    private var presenter: MainContract.Presenter = get{ parametersOf(this) }

    private val dialFragment : DialFragment by inject()
    private val buttonsFragment : MainButtonsFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            configureFragments()
        }
        presenter.onViewCreated()
    }

    override fun initView(){
        bottom_nav.setOnNavigationItemSelectedListener(this)
        hideProgress()
    }

    override fun onFragmentCreated() {
        buttonsFragment.setButtonVisibility(button3,View.GONE)
        buttonsFragment.setButtonVisibility(button4,View.GONE)
        buttonsFragment.setButtonsText(getString(R.string.btn_play),getString(R.string.btn_score))
        animateViews()
    }

    private fun animateViews(){
        alphaViewAnimation(button1,200,1500)
        alphaViewAnimation(button2,950,1500)
    }

    override fun onViewClick(v: View?) {
        when (v!!){
            button1 -> presenter.onQuizzButtonPressed()
            button2 -> presenter.onScoreButtonPressed(
                listOf(getString(R.string.total_pts), getString(R.string.no_score_available),
                    getString(R.string.quiz),getString(R.string.points)))
            btnDial1 -> layoutFragDialMain.visibility = View.GONE
            btnDial2 -> presenter.onReset()
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.menu_item_quit -> presenter.onQuitButtonPressed()
            R.id.menu_item_settings -> presenter.onParamsButtonPressed()
            R.id.menu_item_contact -> presenter.onContactButtonPressed()
        }
        return true
    }

    override fun configureFragments() {
        startSupportFragmentManager(this,dialFragment as BaseFragment,R.id.layoutFragDialMain)
        startSupportFragmentManager(this,buttonsFragment as BaseFragment,R.id.layoutFragMainButtons)
    }

    override fun displayScore(txt : String) {
        layoutFragDialMain.visibility = View.VISIBLE
        dialFragment.display(
            "${getString(R.string.score)} : ", txt, txtNegative =  getString(R.string.return_))
    }

    override fun showProgress() {
        enableOrDisableView(false,rootMain)
        progressMain.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        enableOrDisableView(true,rootMain)
        progressMain.visibility = View.GONE
    }

    override fun onAllQuizzPlayed() {
        layoutFragDialMain.visibility = View.VISIBLE
        dialFragment.display(null,getString(R.string.no_more_quiz),android.R.drawable.stat_sys_warning,
            getString(R.string.cancel),txtPositive = getString(android.R.string.ok) + " !")
    }

    override fun contact() = contact(this)
    override fun quit() = finish()
    override fun goToParams(user: User)  = goToNextActivity(ParamsActivity(),user)
    override fun goToQuizz(user : User) = goToNextActivity(QuizzActivity(),user)
    override fun goToForm(requestCode: Int) {
        enableOrDisableView(false,rootMain)
        startActivityForResult(Intent(this, FormActivity::class.java),requestCode)
    }
    private fun goToNextActivity(appCompatActivity: AppCompatActivity, currentUser : User){
        showProgress()
        goToActivity(this, appCompatActivity,currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            presenter.onUserCreated(data!!,requestCode)
        }else{
            enableOrDisableView(true,rootMain)
        }
    }

    override fun getPresenter(): BasePresenter = presenter
    override fun setPresenter(basePresenter: BasePresenter) {
        this.presenter = basePresenter as MainContract.Presenter
    }

    override fun onResume() {
        super.onResume()
        isConnectionOk(this,this,true)
        enableOrDisableView(true,rootMain)
    }

    override fun onDestroy() {
        presenter.save()
        presenter.onViewDestroyed()
        super.onDestroy()
    }

    override fun onBackPressed() = if(layoutFragDialMain.visibility == View.VISIBLE)
            layoutFragDialMain.visibility = View.GONE
        else
            presenter.onQuitButtonPressed()

}

