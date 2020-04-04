package com.systemathic.flagsquizz.app_repository

import android.content.Intent
import com.systemathic.flagsquizz.model.User

interface UserRepository{

    fun getCurrentUser(intent : Intent?) : User
    fun loadUser() : User
    fun saveUser(user: User)
}