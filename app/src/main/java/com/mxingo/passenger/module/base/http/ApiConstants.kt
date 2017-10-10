package com.mxingo.passenger.module.base.http

/**
 * Created by zhouwei on 2017/6/22.
 */
object ApiConstants {


    const val sign = "Rx-Sign"
    const val token = "Rx-Token"
    const val version = "Rx-Vern"

    const val ip = "http://101.37.34.157:8018/"//线上
//    const val ip = "http://101.37.202.182:8018/"//测试

    const val getVcode = "usr/passenger/getvcode"//获取验证码
    const val login = "usr/passenger/login"//登录
    const val reg = "usr/passenger/reg"//注册
    const val logout = "usr/passenger/logout"//登出
    const val qryFlightiInfo = "usr/passenger/qryflightinfo"//查询航班信息

    const val qryCarLevel = "usr/passenger/qrycarlevel"//车型查询
    const val cancelOrder = "usr/passenger/cancelorder"//取消订单
    const val pubOrder = "usr/passenger/puborder"//发布订单
    const val listCoupon = "usr/passenger/listcoupon"//查询优惠券
    const val listCoupon4Order = "usr/passenger/listcoupon4order"//查询优惠券
    const val qryInvoice = "usr/passenger/qryinvoice"//查询发票申请列表
    const val applyInvoice = "usr/passenger/applyinvoice"//申请发票
    const val qryInvoiceLimit = "usr/passenger/qryinvoicelimit"//发票可开金额
    const val payOrder = "usr/passenger/payorder"//支付订单
    const val payBack = "usr/passenger/payback"//支付结果回调


    const val getInfo = "usr/passenger/getinfo"//获取个人信息

    const val qryOrder = "usr/passenger/qryorder"//获取订单信息
    const val listOrder = "usr/passenger/listorder"//获取行程列表

    const val checkVersion = "comm/checkversion"//检查版本号


}
