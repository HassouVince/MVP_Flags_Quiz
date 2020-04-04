package com.systemathic.flagsquizz.ui.fragments

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.adapters.AdapterParams
import com.systemathic.flagsquizz.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_params_rv.*
import java.lang.ClassCastException

class ParamsRvFragment : BaseFragment(), AdapterParams.Callback {

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseFragment> newInstance(): T = ParamsRvFragment() as T

    override fun getLayoutRessource(): Int = R.layout.fragment_params_rv
    override fun initViews(view: View) {}
    override fun setViews(view: View) {
        configureRv(activity as AdapterParams.Callback)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initItemListener()
    }

    lateinit var mItemClickListener: AdapterParams.Callback
    override fun onItemClickListener(paramName: String) {
        mItemClickListener.onItemClickListener(paramName)
    }

    private fun initItemListener()= try {mItemClickListener = activity as AdapterParams.Callback
    }catch (exception : ClassCastException){
        throw ClassCastException("Interface AdapterParams.Callback must be implemented on activity (Exception : $exception )")
    }

    fun configureRv(clickListener: AdapterParams.Callback){

        val listText = listOf(getString(R.string.cell_params_name),getString(R.string.cell_params_age),getString(R.string.cell_params_update_img),
            getString(R.string.cell_params_delete_img),getString(R.string.cell_params_quit),getString(R.string.cell_params_reset),getString(R.string.cell_params_contact),
            getString(R.string.cell_params_share),getString(R.string.cell_params_home))
        val listResImg = listOf(R.drawable.user,R.drawable.birthday,R.drawable.update_image,R.drawable.delete,R.drawable.quit,R.drawable.delete_cross,
            R.drawable.contact, R.drawable.share,R.drawable.return_icon)

        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = AdapterParams(listText,listResImg,clickListener)
    }
}