package com.systemathic.flagsquizz.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.systemathic.flagsquizz.base.BaseFragment
import com.systemathic.flagsquizz.model.question_pack.QuestionsPack
import com.systemathic.flagsquizz.model.User
import java.lang.Exception
import java.net.URL

var levels = 4

fun displayImageFromPath(imageView: ImageView, pathImage : String){
    if(pathImage.isEmpty()){return}
    val bitmap = BitmapFactory.decodeFile(pathImage)
    imageView.setImageBitmap(bitmap)
}

fun setViewsVisibility(visibility : Int, vararg views : View){
    for(v in views){
        v.visibility = visibility
    }
}

fun enableOrDisableView(enabled : Boolean, v : View){
    v.isEnabled = enabled
    if (v is ViewGroup) {
        for (idx in 0 until v.childCount) {
            enableOrDisableView(enabled,v.getChildAt(idx))
        }
    }
}

fun logD(tag : String, message : String)= try{
    Log.d(tag,message) }catch (e : Exception){}

fun getPreparedIntent(context: Context, targetActivity: AppCompatActivity, vararg objectsToPut : Any) : Intent {
    val intent = Intent(context,targetActivity::class.java)

    for (any in objectsToPut){
        when(any){
            is User -> intent.putExtra(KEY_EXTRAS_USER,any)
            is QuestionsPack -> intent.putExtra(KEY_EXTRAS_PACK,any)
        }
    }
    return intent
}

fun getShareIntent(text : String): Intent {
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain"
    shareIntent.putExtra(
        Intent.EXTRA_TEXT,text
    )
    return shareIntent
}

fun getAnyFromExtras(any: Any,intent: Intent) : Any{
    var anyToreturn = Any()
    if(any is User){
        anyToreturn = intent.getSerializableExtra(KEY_EXTRAS_USER)
    }else if(any is QuestionsPack){
        anyToreturn = intent.getSerializableExtra(KEY_EXTRAS_PACK)
    }
    return anyToreturn
}

fun goToActivity(activity: AppCompatActivity,
                 targetActivity: AppCompatActivity, vararg anys : Any?){
    val intent = getPreparedIntent(activity.applicationContext,targetActivity,anys)
    activity.startActivity(intent)
    activity.finish()
}

fun contact(activity: AppCompatActivity){
    val mails = arrayOf(ADMIN_MAIL)
    val emailIntent = Intent(Intent.ACTION_SEND)
    emailIntent.type = "text/plain"
    emailIntent.putExtra(Intent.EXTRA_EMAIL, mails)
    activity.startActivity(Intent.createChooser(emailIntent, ADMIN_MAIL))
}

fun playSound(context: Context, resSound : Int){
        val mediaPlayer = MediaPlayer.create(context, resSound)
        mediaPlayer.start()
}

/**
 * Return String path folder of the selected image in Android gallery
 */

fun getPath(context: Context, uri: Uri?): String {
    var result: String? = null
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri!!, proj, null, null, null)
    if (cursor != null) {
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(proj[0])
            result = cursor.getString(columnIndex)
        }
        cursor.close()
    }
    if (result == null) {
        result = ""
    }
    Log.d("Utils","Resulte path gallery : $result")
    return result
}


fun startSupportFragmentManager(activity: AppCompatActivity, baseFragment: BaseFragment, idFrameLayout : Int){
    if(activity.supportFragmentManager.findFragmentById(idFrameLayout) == null) {
        activity.supportFragmentManager.beginTransaction()
            .add(idFrameLayout, baseFragment)
            .commit()
    }
}

fun readTextWithUrl(url : String) = URL(url).readText()

fun displayAlertDial(context: Context, title : String? = null, message : String,
                       txtNegative : String, resIcon : Int? = null, txtPositive : String? = null,
                       intentPositive : Intent? = null, runPositive : Runnable? = null,
                       txtNeutral : String? = null, intentNeutral : Intent? = null,
                       runNeutral : Runnable? = null, runNegative : Runnable? = null){

    try {
        val builder = AlertDialog.Builder(context)
        if (title != null) {
            builder.setTitle(title)
        }
        if (resIcon != null) {
            builder.setIcon(resIcon)
        }
        builder.setMessage(message)
        builder.setNegativeButton(txtNegative) { _, _ ->
            if (runNegative != null)
                runNegative.run()

        }
        if (txtPositive != null) {
            builder.setPositiveButton(txtPositive) { _, _ ->
                if (intentPositive != null)
                    context.startActivity(intentPositive)
                 else if (runPositive != null)
                    runPositive.run()
            }
        }
        if (txtNeutral != null) {
            builder.setNeutralButton(txtNeutral) { _, _ ->
                if (intentNeutral != null)
                    context.startActivity(intentNeutral)
                 else if (runNeutral != null)
                    runNeutral.run()

            }
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }catch (e : Exception){e.printStackTrace()}
}