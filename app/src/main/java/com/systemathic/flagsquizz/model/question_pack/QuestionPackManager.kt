package com.systemathic.flagsquizz.model.question_pack

import com.systemathic.flagsquizz.model.question.Question

interface QuestionPackManager {

    fun getQuestionsPacks() : List<QuestionsPack>
    fun getPackWithId(id : Int) : QuestionsPack?
    fun getTabOfQuestionsPack(questions: List<Question>) : List<QuestionsPack>
}