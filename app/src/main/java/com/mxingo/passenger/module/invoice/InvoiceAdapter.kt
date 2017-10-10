package com.mxingo.passenger.module.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mxingo.passenger.R
import com.mxingo.passenger.model.InvoiceEntity
import com.mxingo.passenger.util.TextUtil

/**
 * Created by zhouwei on 2017/8/1.
 */
class InvoiceAdapter() : BaseAdapter() {
    private lateinit var context: Context
    private lateinit var inflater: LayoutInflater
    private var datas: ArrayList<InvoiceEntity> = arrayListOf()


    constructor(context: Context) : this() {
        this.context = context
        inflater = LayoutInflater.from(context)
    }

    fun addAll(datas: List<InvoiceEntity>) {
        datas.forEach {
            it.visibile = View.GONE
        }
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
        val data = datas[position]
        if (convertView == null) {
            v = inflater.inflate(R.layout.item_invoice, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        holder.btnHide.setOnClickListener {
            if (holder.llContentHide.visibility == View.GONE) {
                holder.btnHide.text = "收起"
                holder.imgHide.setImageResource(R.drawable.ic_up_arrow)
                holder.llContentHide.visibility = View.VISIBLE
            } else {
                holder.btnHide.text = "展开"
                holder.imgHide.setImageResource(R.drawable.ic_down_arrow)
                holder.llContentHide.visibility = View.GONE
            }
            data.visibile = holder.llContentHide.visibility
        }

        holder.tvApplyTime.text = TextUtil.getFormatString(data.createTime, "yyyy.MM.dd")
        holder.tvInvoiceStatus.text = InvoiceStatus.getKey(data.status)
        holder.tvInvoiceType.text = InvoiceType.getKey(data.invoiceType)
        if (data.invoiceType == InvoiceType.PERSON_TYPE) {
            holder.llInvoiceName.visibility = View.GONE
            holder.llTaxNumber.visibility = View.GONE
        } else {
            holder.llInvoiceName.visibility = View.VISIBLE
            holder.llTaxNumber.visibility = View.VISIBLE
            holder.tvInvoiceName.text = data.invoiceTitle
            holder.tvTaxNumber.text = data.invoiceCode
        }
        holder.llContentHide.visibility = data.visibile
        holder.tvInvoiceContent.text = data.invoiceContent
        holder.tvInvoiceValue.text = "￥ ${data.invoiceAmount / 100.0}"
        holder.tvAddressee.text = data.name
        holder.tvAddresseeMobile.text = data.mobile
        holder.tvAddresseeAddress.text = "${data.area}${data.addr}"
        return v
    }

    inner class ViewHolder {

        var tvApplyTime: TextView
        var tvInvoiceStatus: TextView
        var tvInvoiceType: TextView
        var tvInvoiceName: TextView
        var llInvoiceName: LinearLayout

        var llTaxNumber: LinearLayout
        var tvTaxNumber: TextView
        var llContentHide: LinearLayout
        var tvInvoiceContent: TextView
        var tvInvoiceValue: TextView

        var tvAddressee: TextView
        var tvAddresseeMobile: TextView

        var tvAddresseeAddress: TextView

        var btnHide: Button
        var imgHide: ImageView


        constructor(view: View) {
            tvApplyTime = view.findViewById(R.id.tv_apply_time)
            tvInvoiceStatus = view.findViewById(R.id.tv_invoice_status)
            tvInvoiceType = view.findViewById(R.id.tv_invoice_type)

            tvInvoiceName = view.findViewById(R.id.tv_invoice_name)
            llInvoiceName = view.findViewById(R.id.ll_invoice_name)
            llTaxNumber = view.findViewById(R.id.ll_tax_number)
            tvTaxNumber = view.findViewById(R.id.tv_tax_number)
            llContentHide = view.findViewById(R.id.ll_content_hide)

            tvInvoiceContent = view.findViewById(R.id.tv_invoice_content)
            tvInvoiceValue = view.findViewById(R.id.tv_invoice_value)
            tvAddressee = view.findViewById(R.id.tv_addressee)
            tvAddresseeMobile = view.findViewById(R.id.tv_addressee_mobile)
            tvAddresseeAddress = view.findViewById(R.id.tv_addressee_address)

            btnHide = view.findViewById(R.id.btn_hide)
            imgHide = view.findViewById(R.id.img_hide)

        }
    }

}