package com.systemathic.flagsquizz.model.question_pack

import com.systemathic.flagsquizz.model.User
import com.systemathic.flagsquizz.model.question.Question
import java.io.Serializable

data class QuestionsPack(var id : Int = 0, val questions : ArrayList<Question> = arrayListOf()) :
    Serializable {

    fun getAvailableQuestionInPack(currentUser : User) : Question {
        if(currentUser.tempQuestionsChecked.size==0){
            return questions[0]
        }
        for(question in questions){
            var find =false
            for(string in currentUser.tempQuestionsChecked){
                if(!find && string.substring(1) == question.id.toString()){
                    find = true
                }
            }
            if(!find){
                return question
            }
        }
        return Question(id = -1)
    }


    fun isQuestionPackCompleted(user : User) : Boolean{
        var i = 0
        if(user.idsQPUsed.size>0) {
            while (i < user.idsQPUsed.size) {
                if (user.idsQPUsed[i].split("|")[1] == id.toString()) {
                    return true
                }
                i++
            }
        }
        return false
    }

    /**
     * Return position of a question pack in a array of pack's ids
     * If no pack finded in the array, the position returned is -1
     */

    fun getIdPositionInIdsArray(list : List<String>) : Int{
        var index = -1
        for(i in list.indices){
            if(list[i].split("|")[1].toInt() == id)
                index = i
        }
        return index
    }
}