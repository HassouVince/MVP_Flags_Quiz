package com.systemathic.flagsquizz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.systemathic.flagsquizz.R

/**
 * Display all settings in ParamsActivity
 */

class AdapterParams(private val arrayTitles : List<String>, private val arrayImages : List<Int>, private val clickListener: Callback)
    : RecyclerView.Adapter<AdapterParams.ViewHolder>(){


    interface Callback{
        fun onItemClickListener(paramName : String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_params, parent, false)
        return ViewHolder(v,clickListener)
    }
    override fun getItemCount() = arrayTitles.size
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) = p0.display(arrayTitles[p1],arrayImages[p1])


    class ViewHolder(itemView: View,private val clickListener: Callback) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener  {

        init {
            itemView.setOnClickListener(this)
        }
        private val tv = itemView.findViewById<View>(R.id.tvCell) as TextView
        private val imgV = itemView.findViewById<View>(R.id.imageCell) as ImageView

        override fun onClick(v: View?) {
            clickListener.onItemClickListener(tv.text.toString())
        }

        fun display(text : String,res : Int){
            tv.text = text
            imgV.setImageResource(res)
        }
    }
}