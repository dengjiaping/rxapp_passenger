package com.mxingo.passenger.module.order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.dialog.MessageDialog
import com.mxingo.passenger.model.CommEntity
import com.mxingo.passenger.model.CommentResultEntity
import com.mxingo.passenger.model.OrderEntity
import com.mxingo.passenger.model.QryOrderEntity
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.util.StartUtil
import com.mxingo.passenger.util.TextUtil
import com.mxingo.passenger.widget.MyProgress
import com.mxingo.passenger.widget.RatingBarView
import com.mxingo.passenger.widget.ShowToast
import com.squareup.otto.Subscribe
import javax.inject.Inject

class OrderInfoActivity : BaseActivity() {

    private lateinit var tvOrderNo: TextView
    private lateinit var tvOrderType: TextView
    private lateinit var tvBookTime: TextView
    private lateinit var tvFlight: TextView
    private lateinit var tvStartAddress: TextView
    private lateinit var tvEndAddress: TextView
    private lateinit var tvAddress: TextView

    private lateinit var llStartAddress: LinearLayout
    private lateinit var llEndAddress: LinearLayout
    private lateinit var llAddress: LinearLayout
    private lateinit var llFlight: LinearLayout

    private lateinit var tvPassenger: TextView
    private lateinit var tvRemark: TextView

    private lateinit var tvPayAmount: TextView
    private lateinit var tvCoupon: TextView
    private lateinit var tvOrderStatus: TextView
    private lateinit var llDriverTake: LinearLayout
    private lateinit var tvCarNo: TextView
    private lateinit var tvDriverName: TextView
    private lateinit var btnCancel: Button
    private lateinit var llPubComment: LinearLayout
    private lateinit var ratingBar: RatingBarView
    private lateinit var etContentComment: EditText
    private lateinit var tvContentComment: TextView
    private lateinit var btnPubComment: Button

    private var dataComment = arrayListOf(0, 0)

    private var order: OrderEntity? = null

    private lateinit var orderNo: String
    var level = 0

    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress

    companion object {
        @JvmStatic
        fun startOrderInfoActivity(activity: Activity, orderNo: String) {
            activity.startActivityForResult(Intent(activity, OrderInfoActivity::class.java)
                    .putExtra(Constants.ACTIVITY_DATA, orderNo), Constants.REQUEST_ORDER_INFO_ACTIVITY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_info)
        orderNo = intent.getStringExtra(Constants.ACTIVITY_DATA)
        progress = MyProgress(this)
        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setToolbar(toolbar)
        val tvToolbar = findViewById(R.id.tv_toolbar_title) as TextView
        tvToolbar.text = "订单详情"

        initView()

        presenter.qryOrder(orderNo)
    }

    private fun initView() {
        tvOrderNo = findViewById(R.id.tv_order_no) as TextView
        tvOrderType = findViewById(R.id.tv_order_type) as TextView
        tvBookTime = findViewById(R.id.tv_book_time) as TextView
        tvFlight = findViewById(R.id.tv_flight) as TextView
        llFlight = findViewById(R.id.ll_flight) as LinearLayout

        tvStartAddress = findViewById(R.id.tv_start_address) as TextView
        tvEndAddress = findViewById(R.id.tv_end_address) as TextView
        tvAddress = findViewById(R.id.tv_address) as TextView

        llStartAddress = findViewById(R.id.ll_start_address) as LinearLayout
        llEndAddress = findViewById(R.id.ll_end_address) as LinearLayout
        llAddress = findViewById(R.id.ll_address) as LinearLayout

        tvPassenger = findViewById(R.id.tv_passenger) as TextView
        tvRemark = findViewById(R.id.tv_remark) as TextView

        tvPayAmount = findViewById(R.id.tv_pay_amount) as TextView
        tvCoupon = findViewById(R.id.tv_coupon) as TextView
        tvOrderStatus = findViewById(R.id.tv_order_status) as TextView
        tvCarNo = findViewById(R.id.tv_car_no) as TextView
        tvDriverName = findViewById(R.id.tv_driver_name) as TextView
        btnCancel = findViewById(R.id.btn_cancel) as Button
        llDriverTake = findViewById(R.id.ll_driver_take) as LinearLayout
        llPubComment = findViewById(R.id.ll_pub_comment) as LinearLayout
        ratingBar = findViewById(R.id.custom_ratingbar) as RatingBarView
        etContentComment = findViewById(R.id.et_content_comment) as EditText
        tvContentComment = findViewById(R.id.tv_content_comment) as TextView
        btnPubComment = findViewById(R.id.btn_pub_comment) as Button

        ratingBar.setOnRatingListener { any: Any?, i: Int ->
            level = i
            if (level > 0) {
                dataComment[0] = 1
                checkView()
            }
        }

        etContentComment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (etContentComment.text.length > 0) {
                    dataComment[1] = 1
                    checkView()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        btnPubComment.setOnClickListener {
            progress.show()
            presenter.evaluate(orderNo, level, etContentComment.text.toString())
        }
        findViewById(R.id.img_mobile).setOnClickListener {
            StartUtil.callMobile(order!!.driverMobile, this)
        }

        btnCancel.setOnClickListener {
            val dialog = MessageDialog(this)
            dialog.setMessageText("确定要取消订单吗?")
            dialog.setOnCancelClickListener {
                dialog.dismiss()
            }
            dialog.setOnOkClickListener {
                dialog.dismiss()
                progress.show()
                presenter.cancelOrder(orderNo)
            }
            dialog.show()
        }
    }

    private fun checkView() {
        if (dataComment[0] + dataComment[1] == 2) {
            btnPubComment.isClickable = true
            btnPubComment.isSelected = true
        } else {
            btnPubComment.isClickable = false
            btnPubComment.isSelected = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unregister(this)
    }

    @Subscribe
    fun loadData(any: Any) {
        when (any::class.java) {
            QryOrderEntity::class.java -> {//查询订单
                progress.dismiss()
                initOrder(any as QryOrderEntity)
            }
            CommEntity::class.java -> {//取消订单
                progress.dismiss()
                val data = any as CommEntity
                if (data.rspCode == "00") {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    ShowToast.showCenter(this, data.rspDesc)
                }
            }
            CommentResultEntity::class.java -> {//评价订单
                progress.dismiss()
                val data = any as CommentResultEntity
                if (data.rspCode == "00") {
                    ShowToast.showCenter(this, data.rspDesc)
                    presenter.qryOrder(orderNo)
                }
            }
        }
    }

    private fun initOrder(qryOrderEntity: QryOrderEntity) {
        if (qryOrderEntity.rspCode.equals("00")) {
            order = qryOrderEntity.order
            tvOrderNo.text = orderNo
            tvOrderType.text = OrderType.getKey(order!!.orderType)
            if (order!!.orderType == OrderType.DAY_RENTER_TYPE) {
                tvBookTime.text = TextUtil.getFormatString(order!!.bookTime, order!!.bookDays, "yyyy年MM月dd日")
                llStartAddress.visibility = View.GONE
                llEndAddress.visibility = View.GONE
                tvAddress.text = order!!.startAddr
            } else {
                tvBookTime.text = TextUtil.getFormatString(order!!.bookTime, "yyyy年MM月dd日 HH:mm")
                llAddress.visibility = View.GONE
                tvStartAddress.text = order!!.startAddr
                tvEndAddress.text = order!!.endAddr
            }
            if (!TextUtil.isEmpty(order!!.tripNo)) {
                tvFlight.text = order!!.tripNo
            } else {
                llFlight.visibility = View.GONE
            }
            tvPassenger.text = "${order!!.passengerName}    ${order!!.passengerMobile}"
            tvRemark.text = order!!.remark
            if (order!!.orderStatus == OrderStatus.CANCELORDER_TYPE) {
                tvPayAmount.text = "¥ ${order!!.orderAmount / 100.0}"
                tvCoupon.visibility = View.GONE
            } else {
                tvPayAmount.text = "¥ ${order!!.payAmount / 100.0}"
                if (order!!.couponAmount != 0) {
                    tvCoupon.text = "用车券抵扣¥${order!!.couponAmount / 100.0}元"
                } else {
                    tvCoupon.visibility = View.GONE
                }
            }
            tvOrderStatus.text = OrderStatus.getKey(order!!.orderStatus)
            if (TextUtil.isEmpty(order!!.driverMobile)) {
                llDriverTake.visibility = View.GONE
            } else {
                tvDriverName.text = order!!.driverName
                tvCarNo.text = order!!.carNo
            }
            if (order!!.orderStatus in OrderStatus.DRIVERARRIVE_TYPE..OrderStatus.FINISH_ORDER_TYPE || order!!.orderStatus == OrderStatus.CANCELORDER_TYPE || order!!.orderStatus == OrderStatus.REFUND_TYPE) {
                btnCancel.visibility = View.GONE
            } else {
                btnCancel.visibility = View.VISIBLE
            }
            if (order!!.orderStatus == OrderStatus.FINISH_ORDER_TYPE) {
                llPubComment.visibility = View.VISIBLE
                if (!TextUtil.isEmpty(order!!.evaluate) && order!!.point != 0) {
                    ratingBar.isClickable = false
                    btnPubComment.visibility = View.GONE
                    etContentComment.visibility = View.GONE
                    tvContentComment.visibility = View.VISIBLE
                    ratingBar.setStar(order!!.point, false)
                    tvContentComment.text = order!!.evaluate
                }
            }
            if (order!!.orderStatus == OrderStatus.PAY_SUCC_TYPE) {
                tvOrderType.append("(${CarType.getKey(order!!.carType)})")
            } else {
                tvOrderType.append("(${CarLevel.getKey(order!!.carLevel)})")
            }
        } else {
            ShowToast.showCenter(this, qryOrderEntity.rspDesc)
        }
    }
}
