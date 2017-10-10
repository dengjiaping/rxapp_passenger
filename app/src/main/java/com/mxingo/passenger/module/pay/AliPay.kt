package com.mxingo.passenger.module.pay

import android.os.Message
import com.alipay.sdk.app.PayTask
import com.mxingo.passenger.module.order.PayOrderActivity


/**
 * Created by zhouwei on 2017/8/7.
 */
class AliPay() {

    private val SDK_PAY_FLAG = 1
    private val SDK_CHECK_FLAG = 2
    private lateinit var activity: PayOrderActivity
    private lateinit var orderNo: String

    constructor(activity: PayOrderActivity) : this() {
        this.activity = activity
    }

    fun requestAliPay(info: String, orderNo: String) {
        this.orderNo = orderNo
        val orderInfo = info   // 订单信息

        val payRunnable = Runnable {
            val alipay = PayTask(this@AliPay.activity)
            val result = alipay.payV2(orderInfo, true)

            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            this@AliPay.activity.mHandler.sendMessage(msg)
        }
        // 必须异步调用
        val payThread = Thread(payRunnable)
        payThread.start()
    }
}