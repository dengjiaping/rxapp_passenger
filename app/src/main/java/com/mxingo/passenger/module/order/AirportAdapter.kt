package com.mxingo.passenger.module.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.baidu.mapapi.search.core.PoiInfo
import com.mxingo.passenger.R
import com.mxingo.passenger.model.AirportEntity
import com.mxingo.passenger.model.QryAirportEntity

/**
 * Created by chendeqiang on 2017/8/1.
 */
class AirportAdapter() : BaseAdapter() {
    private var datas: ArrayList<AirportEntity> = arrayListOf()
    private lateinit var context: Context
    private lateinit var inflater: LayoutInflater

    constructor(context: Context) : this() {
        this.context = context
        inflater = LayoutInflater.from(context)
    }

    fun addAllData(datas: List<AirportEntity>) {
        clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun clear() {
        this.datas.clear()
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val view: View
        var holder: ViewHolder?
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_airport, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        holder.tvPortName.text = datas[position].name
        return view
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

    inner class ViewHolder {

        var tvPortName: TextView


        constructor(view: View) {
            tvPortName = view.findViewById(R.id.tv_airport_name)
        }
    }
}