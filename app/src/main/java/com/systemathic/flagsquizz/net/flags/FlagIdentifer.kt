package com.systemathic.flagsquizz.net.flags

import com.systemathic.flagsquizz.utils.readTextWithUrl
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception

private const val URL_COUNTRIES = "http://country.io/names.json"

data class Country(val name : String, val code : String)

interface FlagIdentiferCallBack{
    fun onFlagIdentiferResult(countries: ArrayList<Country>)
    fun onFlagIdentiferError(errorMessage : String)
}

/**
 * To recover the table of country identifiers to be able to then compare the values ​​with the good answers of the quiz
 */

fun getCountries(fiCallBack: FlagIdentiferCallBack){

    var txt : Deferred<String>?
    var error = "An error occurred"

    GlobalScope.launch(Dispatchers.IO) {
        txt = async {
            try{
                readTextWithUrl(URL_COUNTRIES)
            }catch (e : Exception){
                error = e.toString()
                ""
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            if(txt!!.await().isEmpty())
                fiCallBack.onFlagIdentiferError(error)
            else{
                val countries = ArrayList<Country>()
                val jsonObject = JSONObject(txt!!.await())
                for(indice in jsonObject.keys()){
                    val country = Country(
                        jsonObject.getString(indice),
                        indice
                    )
                    countries.add(country)
                }
                fiCallBack.onFlagIdentiferResult(countries)
            }
        }
    }
}