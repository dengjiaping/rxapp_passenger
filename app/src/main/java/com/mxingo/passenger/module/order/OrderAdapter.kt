package com.mxingo.passenger.module.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mxingo.passenger.R
import com.mxingo.passenger.model.OrderEntity
import com.mxingo.passenger.util.TextUtil

/**
 * Created by zhouwei on 2017/8/3.
 */
class OrderAdapter() : BaseAdapter() {

    private lateinit var context: Context
    private lateinit var inflater: LayoutInflater
    private var datas: ArrayList<OrderEntity> = arrayListOf()


    constructor(context: Context) : this() {
        this.context = context
        inflater = LayoutInflater.from(context)
    }

    fun addAll(datas: List<OrderEntity>) {
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun clear() {
        this.datas.clear()
        notifyDataSetChanged()
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

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val holder: ViewHolder?
        val v: View
        var order: OrderEntity = datas[position]
        if (convertView == null) {
            v = inflater.inflate(R.layout.item_order, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        if (order.orderType == OrderType.DAY_RENTER_TYPE) {
            holder.llBusiness.visibility = View.VISIBLE
            holder.llNoBusiness.visibility = View.GONE
            holder.tvAddress.text = order.startAddr
            holder.tvBookTime.text = TextUtil.getFormatString((order.bookTime)!!.toLong(), order.bookDays, "MM月dd号")

        } else {
            holder.llBusiness.visibility = View.GONE
            holder.llNoBusiness.visibility = View.VISIBLE
            holder.tvStartAddress.text = order.startAddr
            holder.tvEndAddress.text = order.endAddr
            holder.tvBookTime.text = TextUtil.getFormatWeek(order.bookTime.toLong())

            if (order.orderType == OrderType.SEND_PLANE_TYPE) {
                holder.imgStartAddress.setImageResource(R.drawable.ic_address)
                holder.imgEndAddress.setImageResource(R.drawable.ic_airport)
            } else if (order.orderType == OrderType.TAKE_PLANE_TYPE) {
                holder.imgStartAddress.setImageResource(R.drawable.ic_airport)
                holder.imgEndAddress.setImageResource(R.drawable.ic_address)
            } else {
                holder.imgStartAddress.setImageResource(R.drawable.ic_address)
                holder.imgEndAddress.setImageResource(R.drawable.ic_address)
            }
        }

        if (order.carType != 0) {
            holder.tvOrderType.text = "${OrderType.getKey(order.orderType)} (${CarType.getKey(order.carType)}）"
        }
        if (order.carLevel != 0) {
            holder.tvOrderType.text = "${OrderType.getKey(order.orderType)} (${CarLevel.getKey(order.carLevel)}）"
        }

        if (order.orderStatus == OrderStatus.WAIT_PAY_TYPE || order.orderStatus == OrderStatus.CANCELORDER_TYPE || order.orderStatus == OrderStatus.PAY_FAIL_TYPE) {
            holder.tvFee.text = "¥${order.orderAmount / 100.0}"
        } else {
            holder.tvFee.text = "¥${order.payAmount / 100.0}"
        }
        holder.tvOrderStatus.text = OrderStatus.getKey(order.orderStatus)
        return v
    }

    inner class ViewHolder {

        var tvOrderType: TextView
        var tvBookTime: TextView
        var tvAddress: TextView
        var tvStartAddress: TextView
        var tvEndAddress: TextView
        var tvFee: TextView
        var tvOrderStatus: TextView
        var llNoBusiness: LinearLayout
        var llBusiness: LinearLayout
        var imgStartAddress: ImageView
        var imgEndAddress: ImageView


        constructor(view: View) {
            tvOrderType = view.findViewById(R.id.tv_order_type)
            tvBookTime = view.findViewById(R.id.tv_book_time)
            tvStartAddress = view.findViewById(R.id.tv_start_address)
            tvEndAddress = view.findViewById(R.id.tv_end_address)
            tvFee = view.findViewById(R.id.tv_fee)
            tvOrderStatus = view.findViewById(R.id.tv_order_status)
            llNoBusiness = view.findViewById(R.id.ll_no_business)
            llBusiness = view.findViewById(R.id.ll_business)
            tvAddress = view.findViewById(R.id.tv_address)
            imgStartAddress = view.findViewById(R.id.img_start_address)
            imgEndAddress = view.findViewById(R.id.img_end_address)

        }
    }
}