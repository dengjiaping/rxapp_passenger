package com.mxingo.passenger.module.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.mxingo.passenger.R
import com.mxingo.passenger.model.CouponEntity
import com.mxingo.passenger.util.TextUtil

/**
 * Created by zhouwei on 2017/8/1.
 */
class CouponAdapter() : BaseAdapter() {
    private var datas: ArrayList<CouponEntity> = arrayListOf()
    private lateinit var context: Context
    private lateinit var inflater: LayoutInflater

    constructor(context: Context) : this() {
        this.context = context
        inflater = LayoutInflater.from(context)
    }

    fun addAllData(datas: List<CouponEntity>) {
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
            view = inflater.inflate(R.layout.item_coupon, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        val coupon = datas[position]
        holder.tvCouponTitle.text = coupon.title
        if (coupon.couponType == CouponType.MIN_AMOUNT_TYPE) {
            holder.llCouponAmount.visibility = View.VISIBLE
            holder.llCouponDiscount.visibility = View.GONE
            holder.tvCouponAmount.text = "${coupon.value / 100}"
            if (coupon.minOrderAmount != 0) {
                holder.tvUseRule.text = "满${coupon.minOrderAmount / 100.0}元使用"
            } else {
                holder.tvUseRule.text = ""
            }
        } else if (coupon.couponType == CouponType.DISCOUNT_TYPE) {
            holder.llCouponAmount.visibility = View.GONE
            holder.llCouponDiscount.visibility = View.VISIBLE
            holder.tvCouponDiscount.text = if (coupon.value % 10 == 0) { (coupon.value / 10).toString(); } else { (coupon.value / 10.0).toString(); }
            if (coupon.maxDiscountAmout != 0) {
                holder.tvUseRule.text = "最高可减免${coupon.maxDiscountAmout / 100.0}元"
            } else {
                holder.tvUseRule.text = ""
            }
        }

        holder.tvCouponTime.text = "${TextUtil.getFormatString(coupon.startTime, "yyyy.MM.dd")} - ${TextUtil.getFormatString(coupon.expireTime, "yyyy.MM.dd")}"
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

        var tvCouponAmount: TextView
        var tvCouponTitle: TextView
        var tvCouponTime: TextView
        var tvUseRule: TextView
        var tvCouponDiscount: TextView
        var llCouponDiscount: LinearLayout
        var llCouponAmount: LinearLayout

        constructor(view: View) {
            tvCouponAmount = view.findViewById(R.id.tv_coupon_amount)
            tvCouponTitle = view.findViewById(R.id.tv_coupon_title)
            tvCouponTime = view.findViewById(R.id.tv_coupon_time)
            tvUseRule = view.findViewById(R.id.tv_use_rule)
            tvCouponDiscount = view.findViewById(R.id.tv_coupon_discount)
            llCouponDiscount = view.findViewById(R.id.ll_coupon_discount)
            llCouponAmount = view.findViewById(R.id.ll_coupon_amount)
        }

    }

}