package com.systemathic.flagsquizz.base

interface BaseView {
    fun initView()
    fun configureFragments()
    fun showProgress()
    fun hideProgress()
    fun getPresenter() : BasePresenter
    fun setPresenter(basePresenter: BasePresenter)
}