package com.systemathic.flagsquizz.ui.activity.quizz

import com.systemathic.flagsquizz.base.BasePresenter
import com.systemathic.flagsquizz.base.BaseView
import com.systemathic.flagsquizz.model.question.Question
import com.systemathic.flagsquizz.model.question_pack.QuestionsPack
import com.systemathic.flagsquizz.model.User

interface QuizzContract {

    interface View : BaseView{
        fun startQuiz(questionsPack: QuestionsPack)
        fun setVolumeImage(pVolumeOff: Boolean)
        fun displayUserScore()
        fun onAnswerChecked(goodAnswer: String, answerType: QuizzPresenter.AnswersType, soundRes : Int)
        fun displayQuestion(question : Question)
        fun onQuizFinished(points: Int, isAllPlayed : Boolean, soundRes : Int, imgRes : Int)
        fun onNextQuiz()
        fun onRestartQuiz()
        fun onQuit()
        fun displayQuitMsg()
        fun onPreExecute()
        fun onPostExecute(output : QuestionsPack)
        fun onProgressUpdate(txt : String, value: Int?)
        fun onRequestFail(error : String)
    }

    interface Presenter : BasePresenter {
        fun loadQuiz()
        fun onVolumeButtonPressed()
        fun onDialButtonPressed(btnTag: String, dialType : String?)
        fun onAnswerSelected(numAnswer : Int)
        fun isGoodAnswer(numAnswer : Int) : Boolean
        fun onQuitSelected()
        fun quit()
        fun prepareQuestion()
        fun restartQuiz()
        fun finishQuiz()
        fun nextQuiz()
        fun getUser() : User
        fun getQuestion() : Question
        fun getCurrentPack() : QuestionsPack
    }
}