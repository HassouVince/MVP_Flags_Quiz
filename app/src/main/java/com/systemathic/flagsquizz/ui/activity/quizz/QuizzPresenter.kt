package com.systemathic.flagsquizz.ui.activity.quizz


import android.content.Intent
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.app_repository.UserRepositoryImp
import com.systemathic.flagsquizz.model.question.Question
import com.systemathic.flagsquizz.model.question_pack.QuestionsPack
import com.systemathic.flagsquizz.model.User
import com.systemathic.flagsquizz.model.question.QuestionManagerImp
import com.systemathic.flagsquizz.model.question_pack.QuestionPackManagerImp
import com.systemathic.flagsquizz.net.flags.FlagsContract
import com.systemathic.flagsquizz.net.flags.FlagsPresenter
import com.systemathic.flagsquizz.ui.fragments.DialFragment.Companion.TAG_BTN_DIAL_2
import com.systemathic.flagsquizz.ui.fragments.DialFragment.Companion.TAG_BTN_DIAL_3
import com.systemathic.flagsquizz.utils.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.koin.standalone.inject

class QuizzPresenter(private var view: QuizzContract.View? = null)
    : KoinComponent, QuizzContract.Presenter {

    private val userRepos : UserRepositoryImp by inject()
    private val packMgr : QuestionPackManagerImp by inject()
    private val questionMgr : QuestionManagerImp by inject()

    private var currentQuestion : Question = get()
    private lateinit var currentUser : User
    private lateinit var  currentPackQuestion : QuestionsPack

    private var points = 0

    override fun onViewCreated(intent: Intent?) {
        currentUser = getUserFromRepository(intent)
        currentPackQuestion = if (currentUser.idsQPUsed.size == 0) packMgr.getPackWithId(1)!!
        else packMgr.getPackWithId(currentUser.idsQPUsed.size +1)!!
        this.view!!.initView()
    }

    override fun onViewDestroyed() {
        this.view = null
    }

    override fun getUserFromRepository(intent: Intent?): User = userRepos.getCurrentUser(intent)
    override fun getUser() = currentUser
    override fun getQuestion() = currentQuestion
    override fun getCurrentPack() = currentPackQuestion
    override fun save() = userRepos.saveUser(currentUser)

    override fun loadQuiz() {
        FlagsPresenter(view!! as FlagsContract.View,currentPackQuestion).updatePackToPlay()
    }

    override fun onQuizLoaded(pack: QuestionsPack) {
        save()
        this.view!!.startQuiz(pack)
    }

    override fun onVolumeButtonPressed() {
        currentUser.settingsVolumeOff = !currentUser.settingsVolumeOff
        save()
        this.view?.setVolumeImage(currentUser.settingsVolumeOff)
    }

    override fun onDialButtonPressed(btnTag : String, dialType : String?) {
        when(btnTag){
            TAG_BTN_DIAL_2 -> {
                if(dialType != null && dialType == QuizKeys.DIAL_KEY_FINISH.key)
                    restartQuiz()
                else if(dialType != null && dialType == QuizKeys.DIAL_KEY_QUIT.key)
                    quit()
            }
            TAG_BTN_DIAL_3 -> nextQuiz()
        }
    }

    override fun onQuitSelected() = this.view!!.displayQuitMsg()
    override fun quit() {
        save()
        this.view!!.onQuit()
    }

    override fun onAnswerSelected(numAnswer: Int) {
        var point = "0"
        if(isGoodAnswer(numAnswer)){
            currentUser.score++
            point = "1"
            this.view!!.displayUserScore()
        }
        currentUser.tempQuestionsChecked.add("$point${currentQuestion.id}")
        save()
        this.view!!.onAnswerChecked(numAnswer,getAnswerString(numAnswer),getAnswerSoundRes(numAnswer))
    }

    override fun isGoodAnswer(numAnswer: Int) = numAnswer == currentQuestion.indexGoodAnswer
    private fun getAnswerSoundRes(numAnswer: Int) = if(isGoodAnswer(numAnswer)) R.raw.good_answer else R.raw.wrong_answer
    private fun getAnswerString(numAnswer: Int) = if(isGoodAnswer(numAnswer)){
        view!!.provideGoodAnswerMessage()
    }else{
        fun provideComplementMsg() = view!!.provideComplementMessage(currentQuestion.answers[currentQuestion.indexGoodAnswer])
        if(numAnswer == -1){
            "${view!!.provideTimeElapsedMessage()} ${provideComplementMsg()}"
        } else{
            "${view!!.provideWrongAnswerMessage()} ${provideComplementMsg()}"
        }
    }

    override fun prepareQuestion() {
        currentPackQuestion.questions.shuffle()
        currentQuestion = currentPackQuestion.getAvailableQuestionInPack(currentUser)
        if(currentQuestion.answers.isEmpty())
            finishQuiz()
        else{
            try {
                this.view!!.displayQuestion(currentQuestion)
            }catch (e : Exception){}
        }
    }

    override fun nextQuiz() {
        currentPackQuestion = getNextQuestionPack(currentUser)
        this.view!!.onNextQuiz()
    }
    private fun getNextQuestionPack(currentUser: User) = packMgr.getQuestionsPacks()[currentUser.idsQPUsed.size]

    override fun finishQuiz(){
        for(string in currentUser.tempQuestionsChecked){
            points += string.substring(0,1).toInt()
        }
        currentUser.idsQPUsed.add("$points|${currentPackQuestion.id}")
        currentUser.tempQuestionsChecked.clear()
        save()
        this.view!!.onQuizFinished(points, currentUser.isAllPlayed(packMgr,questionMgr)
            ,getFinishSoundRes(points), getFinishImgRes(points))
    }

    override fun restartQuiz() {
        val pos = currentPackQuestion.getIdPositionInIdsArray(currentUser.idsQPUsed)
        if (pos !=-1){
            currentUser.idsQPUsed.removeAt(pos)
        }
        currentUser.score-=points
        save()
        this.view!!.onRestartQuiz()
    }

    private fun getFinishSoundRes(points: Int) = when{
        points == (MAX_QUESTIONS_IN_ONE_PACK) -> R.raw.win
        points < (MAX_QUESTIONS_IN_ONE_PACK / 2) -> R.raw.fail
        else -> R.raw.nice
    }

    private fun getFinishImgRes(points: Int) = when{
        points == (MAX_QUESTIONS_IN_ONE_PACK) -> R.drawable.smiley_winner
        points < (MAX_QUESTIONS_IN_ONE_PACK / 2) ->  R.drawable.smiley_fail
        else -> R.drawable.smiley_nice
    }
}
