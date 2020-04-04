package com.systemathic.flagsquizz.net.flags

import com.systemathic.flagsquizz.model.question_pack.QuestionsPack

interface FlagsContract {
    interface View{
        fun onPreExecute()
        fun onPostExecute(output : QuestionsPack)
        fun onProgressUpdate(txt : String, value: Int?)
        fun onRequestFail(error : String)
    }
    interface Presenter {
        fun updatePackToPlay()
        fun load()
        fun publishProgress(value : Int)
    }

}