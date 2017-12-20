package com.mxingo.passenger.module.base.http

import com.mxingo.passenger.model.*
import com.mxingo.passenger.module.base.log.LogUtils
import com.mxingo.passenger.util.TextUtil
import retrofit2.Callback

/**
 * Created by zhouwei on 2017/6/22.
 */
class MyManger(val apiService: ApiService) {

    fun getVcode(mobile: String, callback: retrofit2.Callback<VCodeEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("mobile", mobile)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("getVcode 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.getVcode(map, headers).enqueue(callback)

    }

    fun login(mobile: String, vcode: String, devType: Int, devToken: String, devInfo: String, callback: retrofit2.Callback<LoginEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("mobile", mobile)
        map.put("vcode", vcode)
        map.put("devType", devType)
        map.put("devToken", devToken)
        map.put("devInfo", devInfo)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("login 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.login(map, headers).enqueue(callback)
    }

    fun reg(mobile: String, vcode: String, devType: Int, devToken: String, devInfo: String, callback: retrofit2.Callback<RegEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("mobile", mobile)
        map.put("vcode", vcode)
        map.put("devType", devType)
        map.put("devToken", devToken)
        map.put("devInfo", devInfo)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("reg 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.reg(map, headers).enqueue(callback)
    }

    fun logout(usrId: Int, callback: retrofit2.Callback<CommEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("usrId", usrId)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("logot 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.logout(map, headers).enqueue(callback)

    }

    fun qryFlightiInfo(tripNo: String, date: Long, callback: retrofit2.Callback<FlightEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("tripNo", tripNo)
        map.put("date", date)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("qrylightiInfo 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.qryFlightiInfo(map, headers).enqueue(callback)

    }

    /**
     * date 格式 yyyyMMddHHmm
     */
    fun qryCarLevel(startLon: Double, startLat: Double, endLon: Double, endLat: Double, orderType: Int, date: String, callback: retrofit2.Callback<QryCarLevelEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("startLon", startLon)
        map.put("startLat", startLat)
        map.put("endLon", endLon)
        map.put("endLat", endLat)
        map.put("orderType", orderType)
        map.put("date", date)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("qryCarLevel 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.qryCarLevel(map, headers).enqueue(callback)
    }

    /**
     * date 格式 yyyy-MM-dd
     */
    fun qryCarLevelDayRenter(city: String, bookDays: Int, orderType: Int, date: String, callback: retrofit2.Callback<QryCarLevelEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("city", city)
        map.put("bookDays", bookDays)
        map.put("orderType", orderType)
        map.put("date", date)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("qryCarLevelDayRenter 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.qryCarLevel(map, headers).enqueue(callback)
    }

    fun cancelOrder(orderNo: String, callback: retrofit2.Callback<CommEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("orderNo", orderNo)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("cancelOrder 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.cancelOrder(map, headers).enqueue(callback)

    }

    fun pubOrder(usrId: Int, carType: String, tripNo: String, airportCode: String, bookTime: Long, bookDays: Int, passengerName: String, passengerMobile: String,
                 orderType: Int, orderAmount: Int, startAddr: String, startLon: Double, startLat: Double, endAddr: String,
                 endLon: Double, endLat: Double, remark: String, productId: String, city: String, callback: retrofit2.Callback<PubOrderEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("usrId", usrId)
        map.put("carType", carType)
        if (!TextUtil.isEmpty(tripNo)) {
            map.put("tripNo", tripNo)
        }
        map.put("airportCode", airportCode)
        map.put("bookTime", bookTime)
        map.put("bookDays", bookDays)
        map.put("passengerName", passengerName)
        map.put("passengerMobile", passengerMobile)
        map.put("orderType", orderType)
        map.put("orderAmount", orderAmount)
        map.put("startAddr", startAddr)
        map.put("startLon", startLon)
        map.put("startLat", startLat)
        map.put("endAddr", endAddr)
        map.put("endLon", endLon)
        map.put("endLat", endLat)
        map.put("productId", productId)
        map.put("startCity", city)
        if (!TextUtil.isEmpty(remark)) {
            map.put("remark", remark)
        }
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("pubOrder 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.pubOrder(map, headers).enqueue(callback)

    }

    fun listCoupon(usrId: Int, pageIndex: Int, pageCount: Int, callback: retrofit2.Callback<ListCouponEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("usrId", usrId)
        map.put("pageIndex", pageIndex)
        map.put("pageCount", pageCount)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("listCoupon 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.listCoupon(map, headers).enqueue(callback)

    }

    fun listCoupon4Order(usrId: Int, orderNo: String, callback: retrofit2.Callback<ListCouponEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("usrId", usrId)
        map.put("orderNo", orderNo)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("listCoupon 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.listCoupon4Order(map, headers).enqueue(callback)

    }

    fun qryInvoice(usrId: Int, pageIndex: Int, pageCount: Int, callback: retrofit2.Callback<QryInvoiceEnitity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("usrId", usrId)
        map.put("pageIndex", pageIndex)
        map.put("pageCount", pageCount)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("qryInvoice 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.qryInvoice(map, headers).enqueue(callback)

    }

    fun qryInvoiceLimit(usrId: Int, callback: retrofit2.Callback<QryInvoiceLimitEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("usrId", usrId)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("qryInvoiceLimit 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.qryInvoiceLimit(map, headers).enqueue(callback)

    }

    fun applyInvoice(usrId: Int, invoiceType: Int, invoiceTitle: String, invoiceCode: String, invoiceAmount: Int,
                     invoiceContent: String, name: String, mobile: String, area: String, addr: String, callback: retrofit2.Callback<CommEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("usrId", usrId)
        map.put("invoiceType", invoiceType)
        map.put("invoiceTitle", invoiceTitle)
        map.put("invoiceCode", invoiceCode)
        map.put("invoiceAmount", invoiceAmount)

        map.put("invoiceContent", invoiceContent)
        map.put("name", name)
        map.put("mobile", mobile)
        map.put("area", area)
        map.put("addr", addr)

        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("applyInvoice 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.applyInvoice(map, headers).enqueue(callback)

    }


    fun payOrder(orderNo: String, coupon: String, payType: Int, callback: retrofit2.Callback<String>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("orderNo", orderNo)
        map.put("couponNo", coupon)
        map.put("payType", payType)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("payOrder 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.payOrder(map, headers).enqueue(callback)

    }

    fun payBack(orderNo: String, orderStatus: Int, callback: retrofit2.Callback<CommEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("orderNo", orderNo)
        map.put("orderStatus", orderStatus)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("payBack 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.payBack(map, headers).enqueue(callback)

    }


    fun qryOrder(orderNo: String, callback: retrofit2.Callback<QryOrderEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("orderNo", orderNo)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("qryOrder 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.qryOrder(map, headers).enqueue(callback)

    }

    fun listOrder(usrId: Int, pageIndex: Int, pageCount: Int, callback: retrofit2.Callback<ListOrderEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("usrId", usrId)
        map.put("pageIndex", pageIndex)
        map.put("pageCount", pageCount)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("listOrder 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.listOrder(map, headers).enqueue(callback)

    }

    fun checkVersion(key: String, callback: retrofit2.Callback<CheckVersionEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("key", key)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("checkVersion 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.checkVersion(map, headers).enqueue(callback)

    }

    fun suggestion(suggestion: String, callback: Callback<CommEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("suggestion", suggestion)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("suggestion 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.suggestion(map, headers).enqueue(callback)
    }

    fun evaluate(orderNo: String, point: Int, evaluate: String, callback: Callback<CommentResultEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("orderNo", orderNo)
        map.put("point", point)
        map.put("evaluate", evaluate)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("evaluate 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.evaluate(map, headers).enqueue(callback)
    }

    fun qryAirport(city: String, callback: Callback<QryAirportEntity>) {
        val map = java.util.TreeMap<String, Any>()
        map.put("city", city)
        val headers = HeaderUtil.getHeaders(map)
        LogUtils.d("qryAirport 参数", map.toString())
        LogUtils.d("headers", headers.toString())
        apiService.qryAirport(map, headers).enqueue(callback)
    }

}