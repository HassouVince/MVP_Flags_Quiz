package com.systemathic.flagsquizz.ui.fragments

import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_loading.*

class LoadingFragment : BaseFragment() {

    private lateinit var  animationTimer : CountDownTimer
    private var radius = 0F

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseFragment> newInstance(): T = LoadingFragment() as T

    override fun getLayoutRessource(): Int = R.layout.fragment_loading
    override fun initViews(view: View) {}
    override fun setViews(view: View) {}

    fun initialize(){
        radius = 0F
        startLoadingAnimation()
    }

    fun updateProgress(txt : String, value: Int?){
        value?.let { (progress_loading_hor as ProgressBar).progress = it }
        tvPourcentLoading.text = txt
    }

    fun startLoadingAnimation(){
        Log.d("animm", "start")
        animationTimer = object: CountDownTimer(100,50) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                radius = getLoadingRadius(radius)
                try{
                    earthQuizzLoad.rotation = radius
                }catch (e : Exception){}

                val frag = this@LoadingFragment
                if(frag.isVisible)
                    startLoadingAnimation()
                else stopLoadingAnimation()
            }
        }
        animationTimer.start()
    }

    fun getLoadingRadius(radius: Float): Float {
        var i = radius
        i+=15F
        if(i == 360F){i = 0F}
        return i
    }

    fun stopLoadingAnimation()= try{animationTimer.cancel()}catch (e : Exception){}
}