package com.mxingo.passenger.module.order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.MyApplication
import com.mxingo.passenger.R
import com.mxingo.passenger.dialog.MessageDialog
import com.mxingo.passenger.model.*
import com.mxingo.passenger.module.base.data.UserInfoPreferences
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.module.base.log.LogUtils
import com.mxingo.passenger.module.coupon.UseCouponActivity
import com.mxingo.passenger.module.pay.AliPay
import com.mxingo.passenger.module.pay.WXPay
import com.mxingo.passenger.util.TextUtil
import com.mxingo.passenger.widget.MyProgress
import com.mxingo.passenger.widget.ShowToast
import com.squareup.otto.Subscribe
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class PayOrderActivity : BaseActivity() {

    private lateinit var tvNeedPay: TextView
    private lateinit var llTotalPay: LinearLayout
    private lateinit var tvTotalPay: TextView
    private lateinit var tvCoupon: TextView
    private lateinit var tvCountDown: TextView
    private lateinit var imgAlipay: ImageView
    private lateinit var imgWechat: ImageView
    private lateinit var btnCancle: Button

    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress
    private lateinit var orderNo: String
    private lateinit var order: OrderEntity

    private val SDK_PAY_FLAG = 1
    private val SDK_CHECK_FLAG = 2

    private var payType = 3
    private var coupon: CouponEntity? = null


    companion object {
        @JvmStatic
        fun startPayOrderActivity(activity: Activity, orderNo: String) {
            activity.startActivityForResult(Intent(activity, PayOrderActivity::class.java)
                    .putExtra(Constants.ACTIVITY_DATA, orderNo),Constants.REQUEST_PAY_ORDER_ACTIVITY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_order)
        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)
        progress = MyProgress(this)
        orderNo = intent.getStringExtra(Constants.ACTIVITY_DATA)

        EventBus.getDefault().register(this)

        setToolbar(toolbar = findViewById(R.id.toolbar) as Toolbar)
        (findViewById(R.id.tv_toolbar_title) as TextView).text = "支付"

        tvNeedPay = findViewById(R.id.tv_need_pay) as TextView
        tvCountDown = findViewById(R.id.tv_count_down) as TextView
        llTotalPay = findViewById(R.id.ll_total_pay) as LinearLayout
        tvTotalPay = findViewById(R.id.tv_total_pay) as TextView
        tvCoupon = findViewById(R.id.tv_coupon) as TextView
        imgAlipay = findViewById(R.id.img_alipay) as ImageView
        imgWechat = findViewById(R.id.img_wechat) as ImageView
        btnCancle = findViewById(R.id.btn_cancel) as Button

        findViewById(R.id.rl_coupon).setOnClickListener {
            UseCouponActivity.startUseCouponActivity(this, UserInfoPreferences.getInstance().userId, orderNo)
        }

        findViewById(R.id.btn_pay).setOnClickListener {
            if (coupon != null && !TextUtil.isEmpty(coupon!!.no)) {
                presenter.payOrder(orderNo, coupon!!.no, payType)
            } else {
                presenter.payOrder(orderNo, "", payType)
            }
        }

        btnCancle.setOnClickListener {
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

        findViewById(R.id.rl_alipay).setOnClickListener {
            payType = 3
            initPayType(payType)
        }

        findViewById(R.id.rl_wechat).setOnClickListener {
            payType = 2
            initPayType(payType)
        }
        initPayType(payType)

        progress.show()
        presenter.qryOrder(orderNo)
        presenter.listCoupon4Order(UserInfoPreferences.getInstance().userId, orderNo)
    }

    private fun initPayType(payType: Int) {
        imgWechat.isSelected = false
        imgAlipay.isSelected = false
        when (payType) {
            3 -> {
                imgAlipay.isSelected = true
            }
            2 -> {
                imgWechat.isSelected = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unregister(this)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun loadData(any: Any) {
        when (any::class.java) {
            QryOrderEntity::class.java -> {
                initOrder(any as QryOrderEntity)
            }
            String::class.java -> {
                startPayOrder(any)
            }
            ListCouponEntity::class.java -> {
                val data = any as ListCouponEntity
                if (data.rspCode.equals("00")) {
                    tvCoupon.text = "有${data.coupons.size}张优惠券可用"
                }
            }
            CommEntity::class.java -> {
                progress.dismiss()
                val data = any as CommEntity
                if (data.rspCode.equals("00")) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun startPayOrder(any: Any) {
        if (payType == 2) {
            val wxPay = Gson().fromJson(any.toString(), WXPayEntity::class.java)
            if (wxPay.rspCode.equals("00")) {
                MyApplication.orderNo = orderNo
                WXPay().requestWXPay(this, wxPay)
            } else {
                ShowToast.showCenter(this, wxPay.rspDesc)
            }
        } else if (payType == 3) {
            val aliPay = Gson().fromJson(any.toString(), AliPayEntity::class.java)
            if (aliPay.rspCode.equals("00")) {
                AliPay(this).requestAliPay(aliPay.od, orderNo)
            } else {
                ShowToast.showCenter(this, aliPay.rspDesc)
            }
        }
    }

    private fun initOrder(qryOrderEntity: QryOrderEntity) {
        progress.dismiss()
        if (qryOrderEntity.rspCode.equals("00")) {
            order = qryOrderEntity.order
            tvNeedPay.text = "¥ ${order.orderAmount / 100.0}"
            if (System.currentTimeMillis() - order.createTime >= 15 * 60 * 1000) {
                finish()
            } else {
                object : CountDownTimer(order.createTime + 15 * 60 * 1000 - java.lang.System.currentTimeMillis(), 1000) {
                    override fun onTick(p0: Long) {
                        val min = p0 / 1000 / 60
                        val sec = p0 / 1000 % 60
                        if (min < 10) {
                            tvCountDown.text = "0$min"
                        } else {
                            tvCountDown.text = "$min"
                        }
                        if (sec < 10) {
                            tvCountDown.append(":0$sec")
                        } else {
                            tvCountDown.append(":$sec")
                        }
                    }

                    override fun onFinish() {
                        progress.show()
                        presenter.cancelOrder(orderNo)
                    }

                }.start()
            }
        } else {
            ShowToast.showCenter(this, qryOrderEntity.rspDesc)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_USE_COUPON_ACTIVITY && resultCode == Activity.RESULT_OK) {
            coupon = data!!.getSerializableExtra(Constants.ACTIVITY_BACK_DATA) as CouponEntity
            llTotalPay.visibility = View.VISIBLE
            tvTotalPay.text = "¥ ${order.orderAmount / 100.0}"
            if (coupon!!.couponType == CouponType.DISCOUNT_TYPE) {
                tvNeedPay.text = "¥ ${order.orderAmount * coupon!!.value / (100 * 100.0)}"
                tvCoupon.text = "已优惠 ${(order.orderAmount - (order.orderAmount * coupon!!.value / 100.0)) / 100}元"
            } else if (coupon!!.couponType == CouponType.MIN_AMOUNT_TYPE) {
                tvNeedPay.text = "¥ ${
                if (order.orderAmount - coupon!!.value < 0) {
                    0.01
                } else {
                    (order.orderAmount - coupon!!.value) / 100.0
                }}"
                tvCoupon.text = "已优惠 ${
                if (order.orderAmount - coupon!!.value < 0) {
                    (order.orderAmount - 1) / 100.0
                } else {
                    coupon!!.value / 100.0
                }}元"
            }
        }
    }


    val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    if (msg.obj == null)
                        return
                   LogUtils.d("map pay result",msg.obj.toString())

                    var payResult = PayResult(TextUtil.getPayResultMap(msg.obj))
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    val resultInfo = payResult.result
                    Log.d("resultInfo", resultInfo + "")
                    val resultStatus = payResult.resultStatus

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (presenter.isRegister) {
                            presenter.payBack(orderNo, OrderStatus.PAY_SUCC_TYPE)
                        }
                        ShowToast.showCenter(this@PayOrderActivity, "支付成功")
                        OrderInfoActivity.startOrderInfoActivity(this@PayOrderActivity, orderNo)
                        finish()
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ShowToast.showCenter(this@PayOrderActivity, "支付中,请稍等")
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            if (presenter.isRegister) {
                                presenter.payBack(orderNo, OrderStatus.PAY_FAIL_TYPE)
                            }
                            ShowToast.showCenter(this@PayOrderActivity, "支付失败")
                        }
                    }
                }

                SDK_CHECK_FLAG -> {
                    if (presenter.isRegister) {
                        presenter.payBack(orderNo, OrderStatus.PAY_FAIL_TYPE)
                    }
                    ShowToast.showCenter(this@PayOrderActivity, "支付失败")
                }

            }
        }
    }

    @org.greenrobot.eventbus.Subscribe
    fun payStatus(code: String) {
        LogUtils.d("code","$code")
        if(code == "1") {
            finish()
        }
    }
}
