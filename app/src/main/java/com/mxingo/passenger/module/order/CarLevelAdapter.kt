package com.mxingo.passenger.module.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mxingo.passenger.R
import com.mxingo.passenger.model.QryCarLevelEntity

/**
 * Created by zhouwei on 2017/8/1.
 */
class CarLevelAdapter() : BaseAdapter() {
    private var datas: ArrayList<QryCarLevelEntity.QueryResultListEntity> = arrayListOf()
    private lateinit var context: Context
    private lateinit var inflater: LayoutInflater

    constructor(context: Context) : this() {
        this.context = context
        inflater = LayoutInflater.from(context)
    }

    fun addAllData(datas: List<QryCarLevelEntity.QueryResultListEntity>) {
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
            view = inflater.inflate(R.layout.item_car_level, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        val data = datas[position]
        holder.tvFee.text = "¥ ${data.Price.toInt()}"
        holder.tvLevelName.text = CarType.getKey(data.VehicleType.toInt())
        holder.tvLevelSize.text = "人数 ≤ ${CarType.getSeat(data.VehicleType.toInt())}"
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

        var tvLevelName: TextView
//        var tvLevelDec: TextView
        var tvLevelSize: TextView
        var tvFee: TextView


        constructor(view: View) {
            tvLevelName = view.findViewById(R.id.tv_level_name)
//            tvLevelDec = view.findViewById(R.id.tv_level_dec)
            tvLevelSize = view.findViewById(R.id.tv_level_size)
            tvFee = view.findViewById(R.id.tv_fee)
        }
    }

}