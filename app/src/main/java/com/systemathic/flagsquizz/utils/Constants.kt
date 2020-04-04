package com.systemathic.flagsquizz.utils

const val MAX_QUESTIONS_IN_ONE_PACK = 5
const val DURATION_CHRONO : Long = 10000
const val DURATION_GOOD_ANSWER_DISPLAYING : Long = 1100
const val DURATION_WRONG_ANSWER_DISPLAYING : Long = 1900
const val FIRST_PART_URL_FLAGS = "https://www.countryflags.io/"
const val SECOND_PART_URL_FLAGS = "/flat/64.png"
const val ADMIN_MAIL = "hassou.vince@gmail.com"
const val ADMIN_LINK = "http://systemathic.somee.com"
const val MAIN_TYPEFONT = "SUGARPUNCH.OTF"

const val KEY_EXTRAS_USER = "current_user"
const val KEY_EXTRAS_PACK = "current_pack"

const val REQUEST_PERMISSION_READ_STORAGE = 100
const val REQUEST_GALLERY = 200

enum class ParamsKeys(val key : String){
    NAME_KEY("name"),
    AGE_KEY("age"),

    DIAL_KEY_RESET("reset"),
    DIAL_KEY_HOME("home"),
    DIAL_KEY_QUIT("quit")
}

enum class QuizKeys(val key : String){
    DIAL_KEY_FINISH("finish_"),
    DIAL_KEY_QUIT("quit")
}