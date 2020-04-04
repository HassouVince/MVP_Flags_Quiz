package com.systemathic.flagsquizz.net.flags

import com.systemathic.flagsquizz.model.question_pack.QuestionsPack
import com.systemathic.flagsquizz.net.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class FlagsPresenter(private var view: FlagsContract.View
                     , private val pack : QuestionsPack)
    : FlagsContract.Presenter
    ,FlagIdentiferCallBack, TranslatorCallback{

    private var fail = false
    private var successOperation = false
    private var answers : List<String> = ArrayList()

    override fun updatePackToPlay() {
        view.onPreExecute()
        load()
        translateCountriesName(this,"FR",getAnswersArray())
    }

    override fun load(){
        GlobalScope.launch(Dispatchers.IO) {
            val success : Deferred<Boolean> = async {
                val durationMax = 300_000
                var i = 0
                while (i<=durationMax && !fail && !successOperation){
                    i+=100
                    delay(100)
                    if(i/100 < 100)
                        publishProgress(i/100)
                }
                publishProgress(100)
                delay(100)
                successOperation
            }

            if(success.await()) {
                GlobalScope.launch(Main) {
                    view.onPostExecute(pack)
                }
            }
        }
    }

    override fun publishProgress(value : Int){
        val valToDisplay = if(value>99 && value!=100){99}
            else{value}
        val txt = "$value%"
        GlobalScope.launch(Main) {
            view.onProgressUpdate(txt, valToDisplay)
        }
    }

    override fun onTranslationDone(translatedText: String?) {
        if(translatedText!=null){
            answers = translatedText.split(" ")
            getCountries(this)
        }else{
            fail = true
            view.onRequestFail("Error translated text is null at : FlagsPresenter -> onTranslationDone")
        }
    }

    override fun onTranslationError(errorMessage: String) {
        fail = true
        view.onRequestFail(errorMessage)
    }

    override fun onFlagIdentiferResult(countries: ArrayList<Country>) {
        updatePack(countries,answers)
        successOperation = true
    }

    override fun onFlagIdentiferError(errorMessage: String) {
        fail = true
        view.onRequestFail(errorMessage)
    }

    /**
     * Return a string list of good answers in english
     * Used to update pack of questions with good coountry Id
     */

    private fun getAnswersArray () : List<String>{
        val list = arrayListOf<String>()
        for(question in pack.questions){
            list.add(question.answers[question.indexGoodAnswer])
        }
        return list
    }

    /**
     * Update appropriate country ID to the array of questionsPacks
     */

    private fun updatePack(countries : ArrayList<Country>, answers : List<String>){
        for((indice,answer) in answers.withIndex()){
            var i = 0
            while(i< countries.size){
                if(answer == countries[i].name){
                    pack.questions[indice].countryCode = countries[i].code
                    break
                }
                i++
            }
        }
    }

}