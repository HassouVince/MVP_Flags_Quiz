package com.systemathic.flagsquizz.net

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.utils.displayAlertDial

fun isConnectionOk(context: Context, activity: AppCompatActivity, dial: Boolean):Boolean{
    val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo=connectivityManager.activeNetworkInfo
    if(networkInfo!=null && networkInfo.isConnected){
        return true
    }else{
        if(dial){
            dialNoConnection(context)
        }else{
            return false
        }
    }
    return true
}

fun dialNoConnection(context: Context) : Boolean{
    displayAlertDial(context,null,context.getString(R.string.no_connection_msg),
        context.getString(R.string.return_),null,context.getString(R.string.parameters),
        Intent(Settings.ACTION_WIRELESS_SETTINGS)
    )
    return false
}