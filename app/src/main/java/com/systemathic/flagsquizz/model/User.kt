package com.systemathic.flagsquizz.model

import android.content.Context
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.model.question.QuestionManagerImp
import com.systemathic.flagsquizz.model.question_pack.QuestionPackManagerImp
import com.systemathic.flagsquizz.utils.MAX_QUESTIONS_IN_ONE_PACK
import com.systemathic.flagsquizz.utils.levels
import java.io.Serializable

data class User(var name : String ="",
                var age : Int = 0,
                var email : String = "",
                var score : Int = 0,
                /**Contains String ( Points + | + id ) of questionsPacks already completed*/
                val idsQPUsed :  ArrayList<String> = ArrayList(levels),
                /**Contains String ( Point(0 or 1) + idQuestion ) of questions played in the current pack */
                val tempQuestionsChecked :  ArrayList<String> = ArrayList(), //
                var pathImage : String = "",
                var settingsVolumeOff : Boolean = false): Serializable {


    fun isRegistred() = name.isNotEmpty() && age >0
    private fun getNbrOfQuestionsCompleted() = (idsQPUsed.size* MAX_QUESTIONS_IN_ONE_PACK) + tempQuestionsChecked.size
    fun isAllPlayed(packManager : QuestionPackManagerImp, questionManager : QuestionManagerImp)
            = (idsQPUsed.size>0 && idsQPUsed.size == packManager.getTabOfQuestionsPack(questionManager.getTabOfQuestions()).size)

    fun getAllScoresToString(
        prefix: String,
        noScoreTxt: String,
        repeatPrefix: String,
        repeatSuffix: String
    ): String {
        var txt =  if(score>0)"$prefix : $score / ${getNbrOfQuestionsCompleted()}\n\n"
        else noScoreTxt
        for(string in idsQPUsed){
            txt += "$repeatPrefix ${string.split("|")[1]} : ${string.split("|")[0]} $repeatSuffix / $MAX_QUESTIONS_IN_ONE_PACK\n\n"
        }
        return txt
    }

    fun getInfosToString(c: Context)= "$name :\n${c.getString(R.string.age)} : $age ${c.getString(
            R.string.years)}" +
                ", ${c.getString(R.string.score)} : $score ${c.getString(R.string.points)}, " +
                "${c.getString(R.string.nbr_quizz_played)} : ${idsQPUsed.size}."

}