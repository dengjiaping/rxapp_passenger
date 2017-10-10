package com.mxingo.passenger.module.pay

import android.content.Context
import com.mxingo.passenger.model.WXPayEntity
import com.mxingo.passenger.widget.ShowToast
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by zhouwei on 2017/8/7.
 */
class WXPay() {

    fun requestWXPay(context: Context, wxPayEntity: WXPayEntity) {
        val api = WXAPIFactory.createWXAPI(context,wxPayEntity.appid)
        val request = PayReq()
        request.appId = wxPayEntity.appid
        request.partnerId = wxPayEntity.partnerid
        request.prepayId = wxPayEntity.prepayid
        request.packageValue = wxPayEntity.packageValue
        request.nonceStr = wxPayEntity.noncestr
        request.timeStamp = wxPayEntity.timestamp
        request.sign = wxPayEntity.sign
        if (!api.sendReq(request)) {
            ShowToast.showCenter(context, "您还没有安装微信")
        }
    }

}