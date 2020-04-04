package com.systemathic.flagsquizz.model.question

import com.systemathic.flagsquizz.utils.MAX_QUESTIONS_IN_ONE_PACK
import com.systemathic.flagsquizz.utils.levels
import com.systemathic.flagsquizz.utils.logD

object QuestionManagerImp : QuestionManager {

    private val TAG = this::class.java.simpleName

    /**
     * Get all questions in one tab
     */

    override fun getTabOfQuestions() : List<Question>{

        for((index,value)in allQuestions.withIndex()){
            value.id = index +1
        }
        logD(TAG,"" + allQuestions.size)
        levels = allQuestions.size/ MAX_QUESTIONS_IN_ONE_PACK

        return allQuestions
    }

    private val allQuestions : List<Question>  by lazy {
        listOf(
            Question(
                listOf(
                    "Pays-Bas",
                    "Irlande",
                    "Slovenie",
                    "Russie"
                ), 3
            ),
            Question(
                listOf(
                    "Turquie",
                    "Canada",
                    "Tunisie",
                    "Suisse"
                ), 0
            ),
            Question(
                listOf(
                    "Belgique",
                    "Espagne",
                    "Argentine",
                    "Chine"
                ), 2
            ),
            Question(
                listOf(
                    "Hongrie",
                    "Pologne",
                    "Ukraine",
                    "Australie"
                ), 1
            ),
            Question(
                listOf(
                    "Croatie",
                    "Bolivie",
                    "Portugal",
                    "Danemark"
                ), 2
            ),

            Question(
                listOf(
                    "Paraguay",
                    "Honduras",
                    "Chili",
                    "Salvador"
                ), 2
            ),
            Question(
                listOf(
                    "Roumanie",
                    "Argentine",
                    "Iran",
                    "Egypte"
                ), 3
            ),
            Question(
                listOf(
                    "Senegal",
                    "Cameroun",
                    "Mali",
                    "Soudan"
                ), 0
            ),
            Question(
                listOf(
                    "Mexique",
                    "Inde",
                    "Luxembourg",
                    "Irelande"
                ), 0
            ),
            Question(
                listOf(
                    "Panama",
                    "Cuba",
                    "Commores",
                    "Haiti"
                ), 1
            ),

            Question(
                listOf(
                    "Venezuela",
                    "Bolivie",
                    "Costa-Rica",
                    "Equateur"
                ), 3
            ),
            Question(
                listOf(
                    "Norvege",
                    "Finlande",
                    "Suede",
                    "Danemark"
                ), 1
            ),
            Question(
                listOf(
                    "Ukraine",
                    "Lettonie",
                    "Moldavie",
                    "Lituanie"
                ), 3
            ),
            Question(
                listOf(
                    "Laos",
                    "Thailande",
                    "Tibet",
                    "Viet-Nam"
                ), 0
            ),
            Question(
                listOf(
                    "Emirats arabes unis",
                    "Kazakhstan",
                    "Dubai",
                    "Madagascare"
                ), 1
            ),

            Question(
                listOf(
                    "Ghana",
                    "Burundi",
                    "Malawi",
                    "Angola"
                ), 3
            ),
            Question(
                listOf(
                    "Tonga",
                    "Samoa",
                    "Panama",
                    "Saint-Marin"
                ), 0
            ),
            Question(
                listOf(
                    "Georgie",
                    "Estonie",
                    "Slovaquie",
                    "Islande"
                ), 1
            ),
            Question(
                listOf(
                    "Micronesie",
                    "Macedoine",
                    "Malte",
                    "Belize"
                ), 2
            ),
            Question(
                listOf(
                    "Cambodge",
                    "Mongolie",
                    "Tibet",
                    "Tadjikistan"
                ), 1
            )
        )
    }
}