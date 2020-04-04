package com.systemathic.flagsquizz.net

import com.systemathic.flagsquizz.utils.readTextWithUrl
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.net.URLEncoder

interface TranslatorCallback{
    fun onTranslationDone(translatedText : String?)
    fun onTranslationError(errorMessage : String)
}

/**
 * This method will make an HTTP request
 * to retrieve the translation of the country name of each good answer in order
 * to look later for the correct country identifier
 */

private fun translate(translatorCallback: TranslatorCallback, text: String, langage : String) {

    val query = URLEncoder.encode(text, "UTF-8")
    val langpair = URLEncoder.encode("$langage|EN", "UTF-8")
    val url = "http://mymemory.translated.net/api/get?q=$query&langpair=$langpair"
    var txt : Deferred<String>?
    var error = ""

    GlobalScope.launch(IO) {
        txt = async {
            try{
                readTextWithUrl(url)
            }catch (e : Exception){
                error = e.toString()
                ""
            }
        }

        GlobalScope.launch(Main) {
            if(txt!!.await().isEmpty())
                translatorCallback.onTranslationError(error)
            else {
                val jsonObject = JSONObject(txt!!.await())
                val translated = jsonObject.getJSONObject("responseData")
                    .getString("translatedText")
                translatorCallback.onTranslationDone(translated)
            }
        }
    }
}

fun translateCountriesName(translatorCallback: TranslatorCallback,
                           userLanguage : String, answers : List<String>){
    var answersTxt = ""
    for(answer in answers){
        answersTxt += " $answer"
    }
    translate(translatorCallback,answersTxt,userLanguage)
}
