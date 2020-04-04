package com.systemathic.flagsquizz.model.question_pack

import com.systemathic.flagsquizz.model.question.Question
import com.systemathic.flagsquizz.model.question.QuestionManagerImp
import com.systemathic.flagsquizz.utils.MAX_QUESTIONS_IN_ONE_PACK
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

object QuestionPackManagerImp : QuestionPackManager, KoinComponent {

    private val questionRepos : QuestionManagerImp by inject()

    override fun getQuestionsPacks() : List<QuestionsPack>{
        return getTabOfQuestionsPack(questionRepos.getTabOfQuestions())
    }

    /**
     * Get arrayList of packs questions
     */

    override fun getTabOfQuestionsPack(questions: List<Question>) : List<QuestionsPack>{
        val packs = ArrayList<QuestionsPack>()
        var count = 0
        for(value in questions){
            if(count>= MAX_QUESTIONS_IN_ONE_PACK){ count = 0 }
            if(count == 0){
                packs.add(
                    QuestionsPack(
                        0,
                        ArrayList()
                    )
                )
                packs[packs.size-1].id = packs.size
            }
            packs[packs.size-1].questions.add(value)
            count++
        }

        return packs
    }

    /**
     * Find object QustionPack with is Id
     */

    override fun getPackWithId(id : Int) : QuestionsPack? {
        val packs = getTabOfQuestionsPack(questionRepos.getTabOfQuestions())
        var indice = -1
        for((i,pack) in packs.withIndex()){
            if(pack.id == id){
                indice = i
            }
        }
        return if(indice!=-1) packs[indice] else null
    }

}