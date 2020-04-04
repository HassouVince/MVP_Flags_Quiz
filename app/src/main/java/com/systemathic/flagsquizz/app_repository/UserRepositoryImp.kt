package com.systemathic.flagsquizz.app_repository

import android.content.Intent
import android.content.SharedPreferences
import com.google.gson.Gson
import com.systemathic.flagsquizz.model.User
import com.systemathic.flagsquizz.utils.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.lang.Exception

object UserRepositoryImp : UserRepository, KoinComponent {

    private val sharedPreferences: SharedPreferences by inject()

    override fun saveUser(user: User){
        try {
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(user)
            editor.putString(user.javaClass.simpleName, json)
            editor.apply()
        }catch (e : Exception){}
    }

    override fun loadUser(): User = try{
            val gson = Gson()
            val json = sharedPreferences.getString(User().javaClass.simpleName, "")
            gson.fromJson<User>(json, User::class.java)
        }catch (e : Exception){
            User()
        }

    override fun getCurrentUser(intent : Intent?) : User {
        var currentUser  = User()
        currentUser = try {
            getAnyFromExtras(currentUser,intent!!) as User
        }catch (e : Exception){
            try {
                loadUser()
            }catch (ex : Exception){User()}
        }
        return currentUser
    }
}