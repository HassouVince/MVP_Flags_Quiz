package com.systemathic.flagsquizz.model.question

import java.io.Serializable

data class Question(val answers : List<String> = listOf(), val indexGoodAnswer : Int = 0,
                    var countryCode : String = "EN", var id : Int = 0) : Serializable