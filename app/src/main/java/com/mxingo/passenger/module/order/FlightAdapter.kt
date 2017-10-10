package com.mxingo.passenger.module.order

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mxingo.passenger.R
import com.mxingo.passenger.util.DisplayUtil

/**
 * Created by zhouwei on 2017/8/1.
 */
class FlightAdapter() : BaseAdapter() {
    private var datas: ArrayList<String> = arrayListOf()
    private lateinit var context: Context

    constructor(context: Context) : this() {
        this.context = context
    }

    fun addAllData(datas:ArrayList<String>){
        this.datas.clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun clear(){
        this.datas.clear()
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val v: TextView
        if (convertView == null) {
            v = TextView(context)
            v.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context, 40f))

        } else {
            v = convertView as TextView
        }
        v.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        v.gravity = Gravity.CENTER_VERTICAL
        v.setPadding(DisplayUtil.dip2px(context,20f),0,0,0)
        v.text = datas[position]
        return v
    }

    override fun getItem(position: Int): Any {
        return datas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return datas.size
    }

}