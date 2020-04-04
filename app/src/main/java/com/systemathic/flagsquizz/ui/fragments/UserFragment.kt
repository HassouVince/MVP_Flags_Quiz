package com.systemathic.flagsquizz.ui.fragments

import android.view.View
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.base.BaseFragment
import com.systemathic.flagsquizz.utils.displayImageFromPath
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : BaseFragment() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseFragment> newInstance(): T = UserFragment() as T

    override fun getLayoutRessource(): Int = R.layout.fragment_user
    override fun initViews(view: View) {}
    override fun setViews(view: View) { callback.onFragmentCreated()}

    fun setImage(path : String?){
        if(path != null)
            displayImageFromPath(imageUser,path)
        else
            setImage(R.drawable.user_no_image)
    }

    fun setImage(resImage : Int){
        imageUser.setImageResource(resImage)
    }

    fun setText(txt: String){
        tvUser.text = txt
    }
}