package com.systemathic.flagsquizz.base

import android.content.Intent
import com.systemathic.flagsquizz.model.User

interface BasePresenter {
    fun onViewCreated(intent: Intent? = null)
    fun onViewDestroyed()
    fun save()
    fun getUserFromRepository(intent: Intent?) : User
}