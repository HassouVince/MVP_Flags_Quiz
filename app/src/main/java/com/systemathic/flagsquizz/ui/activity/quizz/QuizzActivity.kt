package com.systemathic.flagsquizz.ui.activity.quizz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_quizz.*
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.base.BaseFragment
import com.systemathic.flagsquizz.base.BasePresenter
import com.systemathic.flagsquizz.extensions.toast
import com.systemathic.flagsquizz.model.question.Question
import com.systemathic.flagsquizz.model.question_pack.QuestionsPack
import com.systemathic.flagsquizz.net.flags.FlagsContract
import com.systemathic.flagsquizz.net.isConnectionOk
import com.systemathic.flagsquizz.ui.activity.main.MainActivity
import com.systemathic.flagsquizz.ui.alphaViewAnimation
import com.systemathic.flagsquizz.ui.fragments.*
import com.systemathic.flagsquizz.ui.fromTopAnimation
import com.systemathic.flagsquizz.utils.*
import kotlinx.android.synthetic.main.fragment_buttons.*
import kotlinx.android.synthetic.main.fragment_clickable_image.*
import kotlinx.android.synthetic.main.fragment_dial.*
import kotlinx.android.synthetic.main.fragment_presentation.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.lang.Exception


class QuizzActivity : AppCompatActivity(), QuizzContract.View,FlagsContract.View,
    BaseFragment.OnViewClickListener, BaseFragment.CallbackOnFragmentCreated
    ,PresentationFragment.ImageLoadCallback {

    private var presenter: QuizzContract.Presenter = get{ parametersOf(this) }

    private val presentationFragment: PresentationFragment by inject()
    private val buttonsFragment: MainButtonsFragment by inject()
    private val userFragment: UserFragment by inject()
    private val clickableImageFragment: ClickableImageFragment by inject()
    private val loadingFragment: LoadingFragment by inject()
    private val dialFragment: DialFragment by inject()

    private lateinit var chrono: CountDownTimer
    private var quizFinished = false
    private var quizPaused = false
    private var currentDialChoice = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz)
        if (savedInstanceState == null) {
            configureFragments()
        }
        presenter.onViewCreated()
    }

    override fun initView() = setDialVisibility(View.GONE)

    override fun onFragmentCreated() {
        try {
            setVolumeImage(presenter.getUser().settingsVolumeOff)
            presentationFragment.setImage(R.drawable.wait)
            userFragment.setImage(presenter.getUser().pathImage)
            displayUserScore()
            presentationFragment.setTitle("Niveau ${presenter.getCurrentPack().id}")
            if(isConnectionOk(this,this,true))
                presenter.loadQuiz()
        } catch (e: Exception) {e.printStackTrace()}
    }

    override fun configureFragments() {
        startSupportFragmentManager(this, clickableImageFragment as BaseFragment, R.id.layoutFragQuizVolume)
        startSupportFragmentManager(this, presentationFragment as BaseFragment, R.id.layoutFragQuizPresentation)
        startSupportFragmentManager(this, buttonsFragment as BaseFragment, R.id.layoutFragQuizButtons)
        startSupportFragmentManager(this, userFragment as BaseFragment, R.id.layoutFragQuizUser)
        startSupportFragmentManager(this, loadingFragment as BaseFragment, R.id.layoutFragQuizLoading)
        startSupportFragmentManager(this, dialFragment as BaseFragment, R.id.layoutFragQuizzDial)
    }

    override fun onViewClick(v: View?) {
        when (v) {
            button1 -> presenter.onAnswerSelected(0)
            button2 -> presenter.onAnswerSelected(1)
            button3 -> presenter.onAnswerSelected(2)
            button4 -> presenter.onAnswerSelected(3)
            btnDial1 -> layoutFragQuizzDial.visibility = View.GONE
            btnDial2 -> presenter.onDialButtonPressed(btnDial2.tag.toString(),currentDialChoice)
            btnDial3 -> presenter.onDialButtonPressed(btnDial3.tag.toString(),null)
            imageClickableImage -> presenter.onVolumeButtonPressed()
        }
    }

    override fun getPresenter(): BasePresenter = presenter
    override fun setPresenter(basePresenter: BasePresenter) {
        presenter = basePresenter as QuizzContract.Presenter
    }
    private fun setLoadingLayoutVisibility(visibility: Int) { layoutFragQuizLoading.visibility = visibility }
    private fun setButtonsVisibility(visibility: Int) { layoutFragQuizButtons.visibility = visibility }
    private fun setDialVisibility(visibility: Int) { layoutFragQuizzDial.visibility = visibility }

    override fun startQuiz(questionsPack: QuestionsPack){
        if(questionsPack.id == -1){
            toast(getString(R.string.error))
            presenter.onQuitSelected()
            return
        }
        setButtonsVisibility(View.GONE)
        presenter.prepareQuestion()
    }

    override fun displayQuestion(question : Question){
        if(question.answers.isEmpty()){
            presenter.finishQuiz()
            return
        }
        setButtonsVisibility(View.GONE)
        presentationFragment.setText(getString(R.string.img_load))
        buttonsFragment.setButtonsText(texts = question.answers)
        presentationFragment.setImage(R.drawable.wait)
        val url = FIRST_PART_URL_FLAGS + question.countryCode + SECOND_PART_URL_FLAGS
        presentationFragment.setImageWithUrl(url)
    }

    override fun onImageLoadSuccess() {
        setViewsVisibility(View.VISIBLE,layoutFragQuizButtons,layoutFragQuizPresentation,
            layoutFragQuizUser,layoutFragQuizVolume,progress_chrono_quizz)
        buttonsFragment.enableOrDisableAllButtons(true)
        buttonsFragment.setAllButtonsBackGround(R.drawable.rounded_button)
        presentationFragment.setText(getString(R.string.question))
        animateViews()
        startChrono()
    }

    private fun animateViews(){
        try {
            fromTopAnimation(imagePresentation,200,1000)
            for(button in buttonsFragment.buttons)
                alphaViewAnimation(button,200,1000)
        }catch (e : Exception){e.printStackTrace()}
    }

    override fun onImageLoadError(e : Exception?) {
        e.let { Log.e("image_load",e.toString())}
        toast(getString(R.string.error))
        presenter.onQuitSelected()
    }

    override fun onAnswerChecked(numAnswer: Int, msg : String, soundRes : Int) {
        stopChrono()
        initChronoProgress()
        setViewsVisibility(View.GONE,progress_chrono_quizz)
        buttonsFragment.enableOrDisableAllButtons(false)
        buttonsFragment.setButtonBackGround(R.drawable.rounded_button_good_answer,presenter.getQuestion().indexGoodAnswer)
        presentationFragment.setText(msg)
        playSound(soundRes)
        freezeAfterAnswer(numAnswer)
    }

    private fun freezeAfterAnswer(numAnswer: Int){
        fun getDurationScreenFreeze() = if(presenter.isGoodAnswer(numAnswer)){ DURATION_GOOD_ANSWER_DISPLAYING }
        else{ DURATION_WRONG_ANSWER_DISPLAYING }
        val timer2 = object: CountDownTimer(getDurationScreenFreeze(),500) { override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {presenter.prepareQuestion()} }
        timer2.start()
    }

    override fun onQuizFinished(points: Int, isAllPlayed : Boolean, soundRes : Int,
                                imgRes : Int) {
        quizFinished = true
        presentationFragment.setText(getString(R.string.quizz_finish))
        presentationFragment.setImage(R.drawable.eart)
        setButtonsVisibility(View.GONE)
        displayFinishMessage(points,isAllPlayed,imgRes)
        if(!presenter.getUser().settingsVolumeOff) {playSound(soundRes)}
    }

    private fun displayFinishMessage(points : Int, isAllPlayed : Boolean,imgRes : Int){
        val msg = "${getString(R.string.game_finish)}\n${getString(R.string.score)} : $points / $MAX_QUESTIONS_IN_ONE_PACK"
        val txtB2 = getString(R.string.restart_quizz)
        val txtB3 : String? = if(isAllPlayed) null else getString(R.string.continue_)
        showDial(QuizKeys.DIAL_KEY_FINISH.key,getString(R.string.finish_) + " !",
            msg,imgRes,getString(R.string.cancel),txtB2,txtB3)
    }

    override fun displayQuitMsg() = showDial(QuizKeys.DIAL_KEY_QUIT.key, msg ="${getString(R.string.quit)} ?",
            txtB1 = getString(R.string.cancel),txtB2 = getString(R.string.quit))

    private fun showDial(dialChoice : String, title : String? = null, msg : String,
                         resImage : Int? = null, txtB1 : String? = null, txtB2 : String? = null,
                         txtB3 : String? = null){
        setDialVisibility(View.VISIBLE)
        dialFragment.display(title,msg,resImage,txtNegative = txtB1!!,
            txtPositive = txtB2, txtNeutral = txtB3)
        currentDialChoice = dialChoice
    }

    private fun startChrono(){
        chrono = object: CountDownTimer(DURATION_CHRONO, DURATION_CHRONO/100) {
            override fun onTick(millisUntilFinished: Long) {
                updateChronoProgress()
            }
            override fun onFinish() {
                presenter.onAnswerSelected(-1)
                initChronoProgress()
            }
        }
        chrono.start()
    }

    private fun stopChrono() = try {chrono.cancel()}catch (e : Exception){e.printStackTrace()}
    override fun showProgress() { progressQuizz.visibility = View.VISIBLE }
    override fun hideProgress() { progressQuizz.visibility = View.GONE }
    private fun initChronoProgress(){(progress_chrono_quizz as ProgressBar).progress = 0}
    private fun updateChronoProgress() {(progress_chrono_quizz as ProgressBar).progress+=1}
    override fun provideGoodAnswerMessage(): String = getString(R.string.good_answer)
    override fun provideWrongAnswerMessage(): String = getString(R.string.wrong)
    override fun provideTimeElapsedMessage(): String = getString(R.string.time_elapsed)
    override fun provideComplementMessage(goodAnswer : String): String =
       " ${getString(R.string.good_answer_was)} : $goodAnswer "
    override fun displayUserScore() = userFragment.setText("${presenter.getUser().score} pts")
    override fun setVolumeImage(pVolumeOff: Boolean)= if(pVolumeOff) clickableImageFragment.setImage(R.drawable.util_volume)
    else clickableImageFragment.setImage(R.drawable.util_volume_mute)
    override fun onNextQuiz()= goToActivity(this,QuizzActivity(),presenter.getUser(),presenter.getCurrentPack())
    override fun onRestartQuiz() = goToActivity(this,QuizzActivity(),presenter.getUser(),presenter.getCurrentPack())
    private fun playSound(res : Int) = if(!presenter.getUser().settingsVolumeOff){ playSound(this,res) }else{}

    override fun onQuit(){
        showProgress()
       // presentationFragment.setImage(R.drawable.wait)
        setViewsVisibility(View.GONE,layoutFragQuizPresentation,layoutFragQuizButtons,
            layoutFragQuizUser,layoutFragQuizVolume,progress_chrono_quizz)
        startActivity(getPreparedIntent(this, MainActivity(), presenter.getUser()))
        finish()
    }

    override fun onBackPressed() {
        if(!quizFinished)
            presenter.onQuitSelected()
        else
            presenter.quit()
    }

    override fun onPause() {
        super.onPause()
        quizPaused = true
        stopChrono()
    }

    override fun onResume() {
        super.onResume()
        if(quizPaused && !quizFinished){
            initChronoProgress()
            setButtonsVisibility(View.GONE)
            presenter.restartQuiz()
        }else{
            quizPaused = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.save()
        presenter.onViewDestroyed()
    }

    ////// ASYNC CALLBACK

    override fun onPreExecute() {
        setButtonsVisibility(View.GONE)
        setLoadingLayoutVisibility(View.VISIBLE)
        loadingFragment.initialize()
    }

    override fun onPostExecute(output : QuestionsPack) {
        setLoadingLayoutVisibility(View.GONE)
        setViewsVisibility(View.VISIBLE,layoutFragQuizButtons,layoutFragQuizPresentation,
            layoutFragQuizUser,layoutFragQuizVolume)
        loadingFragment.stopLoadingAnimation()
        presenter.onQuizLoaded(output)
    }

    override fun onRequestFail(error: String) {
        Toast.makeText(this,error,Toast.LENGTH_LONG).show()
        presenter.quit()
    }

    override fun onProgressUpdate(txt : String, value: Int?) = loadingFragment.updateProgress(txt,value)
}
