package com.mxingo.passenger.module.base.http

import com.mxingo.passenger.model.*
import com.mxingo.passenger.module.base.log.LogUtils
import com.mxingo.passenger.module.order.InvoiceType
import com.mxingo.passenger.util.TextUtil
import com.squareup.otto.Bus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NAME_SHADOWING")
/**
 * Created by zhouwei on 2017/6/22.
 */
class MyPresenter(private val mBs: Bus, private val manger: MyManger) {

    var isRegister = true

    fun register(any: Any) {
        mBs.register(any)
        isRegister = true
    }

    fun unregister(any: Any) {
        mBs.unregister(any)
        isRegister = false
    }

    fun getVcode(mobile: String) {
        if (!TextUtil.isMobileNO(mobile)) {
            val data = CommEntity()
            data.rspCode = "1000"
            data.rspDesc = "请输入正确号码"
            mBs.post(data)
            return
        }
        manger.getVcode(mobile, object : Callback<VCodeEntity> {
            override fun onResponse(call: Call<VCodeEntity>, response: Response<VCodeEntity>) {
                if (response.body() != null) {
                    LogUtils.d("getVcode", response.body().toString())
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<VCodeEntity>, t: Throwable) {
                val data = VCodeEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun login(mobile: String, vcode: String, devType: Int, devToken: String, devInfo: String) {
        if (!TextUtil.isMobileNO(mobile)) {
            val data = CommEntity()
            data.rspCode = "1000"
            data.rspDesc = "请输入正确手机号"
            mBs.post(data)
        }

        if (TextUtil.isEmpty(vcode)) {
            val data = CommEntity()
            data.rspCode = "1000"
            data.rspDesc = "验证码不能为空"
            mBs.post(data)
        }

        manger.login(mobile, vcode, devType, devToken, devInfo, object : Callback<LoginEntity> {
            override fun onResponse(call: Call<LoginEntity>, response: Response<LoginEntity>) {
                LogUtils.d("login", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<LoginEntity>, t: Throwable) {
                val data = LoginEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun reg(mobile: String, vcode: String, devType: Int, devToken: String, devInfo: String) {
        if (!TextUtil.isMobileNO(mobile)) {
            val data = CommEntity()
            data.rspCode = "1000"
            data.rspDesc = "请输入正确手机号"
            mBs.post(data)
        }

        if (TextUtil.isEmpty(vcode)) {
            val data = CommEntity()
            data.rspCode = "1000"
            data.rspDesc = "验证码不能为空"
            mBs.post(data)
        }

        manger.reg(mobile, vcode, devType, devToken, devInfo, object : Callback<RegEntity> {
            override fun onResponse(call: Call<RegEntity>, response: Response<RegEntity>) {
                LogUtils.d("login", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<RegEntity>, t: Throwable) {
                val data = RegEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }


    fun logout(usrId: Int) {
        manger.logout(usrId, object : Callback<CommEntity> {
            override fun onResponse(call: Call<CommEntity>, response: Response<CommEntity>) {
                LogUtils.d("logout", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<CommEntity>, t: Throwable) {
                val data = CommEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun qryFlightiInfo(tripNo: String, date: Long) {
        manger.qryFlightiInfo(tripNo, date, object : Callback<FlightEntity> {
            override fun onResponse(call: Call<FlightEntity>, response: Response<FlightEntity>) {
                LogUtils.d("qryFlightiInfo", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<FlightEntity>, t: Throwable) {
                val data = FlightEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun qryCarLevel(startLon: Double, startLat: Double, endLon: Double, endLat: Double, orderType: Int, date: String) {
        manger.qryCarLevel(startLon, startLat, endLon, endLat, orderType, date, object : Callback<QryCarLevelEntity> {
            override fun onResponse(call: Call<QryCarLevelEntity>, response: Response<QryCarLevelEntity>) {
                LogUtils.d("qryCarLevel", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<QryCarLevelEntity>, t: Throwable) {
                val data = QryCarLevelEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun qryCarLevelDayRenter(city: String, bookDays: Int, orderType: Int, date: String) {
        manger.qryCarLevelDayRenter(city, bookDays, orderType, date, object : Callback<QryCarLevelEntity> {
            override fun onResponse(call: Call<QryCarLevelEntity>, response: Response<QryCarLevelEntity>) {
                LogUtils.d("qryCarLevelDayRenter", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<QryCarLevelEntity>, t: Throwable) {
                val data = QryCarLevelEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun cancelOrder(orderNo: String) {
        manger.cancelOrder(orderNo, object : Callback<CommEntity> {
            override fun onResponse(call: Call<CommEntity>, response: Response<CommEntity>) {
                LogUtils.d("cancelOrder", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<CommEntity>, t: Throwable) {
                val data = CommEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun pubOrder(usrId: Int, carType: String, tripNo: String, airportCode: String, bookTime: Long, bookDays: Int, passengerName: String, passengerMobile: String,
                 orderType: Int, orderAmount: Int, startAddr: String, startLon: Double, startLat: Double, endAddr: String,
                 endLon: Double, endLat: Double, remark: String, productId: String, city: String) {
        manger.pubOrder(usrId, carType, tripNo, airportCode, bookTime, bookDays, passengerName, passengerMobile, orderType, orderAmount, startAddr,
                startLon, startLat, endAddr, endLon, endLat, remark, productId, city, object : Callback<PubOrderEntity> {
            override fun onResponse(call: Call<PubOrderEntity>, response: Response<PubOrderEntity>) {
                LogUtils.d("pubOrder", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<PubOrderEntity>, t: Throwable) {
                val data = PubOrderEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun listCoupon(usrId: Int, pageIndex: Int, pageCount: Int) {
        manger.listCoupon(usrId, pageIndex, pageCount, object : Callback<ListCouponEntity> {
            override fun onResponse(call: Call<ListCouponEntity>, response: Response<ListCouponEntity>) {
                LogUtils.d("listCoupon", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<ListCouponEntity>, t: Throwable) {
                val data = ListCouponEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun listCoupon4Order(usrId: Int, orderNo: String) {
        manger.listCoupon4Order(usrId, orderNo, object : Callback<ListCouponEntity> {
            override fun onResponse(call: Call<ListCouponEntity>, response: Response<ListCouponEntity>) {
                LogUtils.d("listCoupon4Order", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<ListCouponEntity>, t: Throwable) {
                val data = ListCouponEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun qryInvoice(usrId: Int, pageIndex: Int, pageCount: Int) {
        manger.qryInvoice(usrId, pageIndex, pageCount, object : Callback<QryInvoiceEnitity> {
            override fun onResponse(call: Call<QryInvoiceEnitity>, response: Response<QryInvoiceEnitity>) {
                LogUtils.d("qryInvoice", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<QryInvoiceEnitity>, t: Throwable) {
                val data = QryInvoiceEnitity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun qryInvoiceLimit(usrId: Int) {
        manger.qryInvoiceLimit(usrId, object : Callback<QryInvoiceLimitEntity> {
            override fun onResponse(call: Call<QryInvoiceLimitEntity>, response: Response<QryInvoiceLimitEntity>) {
                LogUtils.d("qryInvoiceLimit", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<QryInvoiceLimitEntity>, t: Throwable) {
                val data = QryInvoiceLimitEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun applyInvoice(usrId: Int, invoiceType: Int, invoiceTitle: String, invoiceCode: String, invoiceAmount: String,
                     invoiceAmtLimit: Int, invoiceContent: String, name: String, mobile: String, area: String, addr: String) {
        val data = CommEntity()
        data.rspCode = "1000"
        if (invoiceType == InvoiceType.COMPANY_TYPE) {
            if (TextUtil.isEmpty(invoiceTitle)) {
                data.rspDesc = "发票内容名称不能为空"
                mBs.post(data)
                return
            }
            if (TextUtil.isEmpty(invoiceCode)) {
                data.rspDesc = "纳税人识别号不能为空"
                mBs.post(data)
                return
            }
        }

        if (TextUtil.isEmpty(invoiceAmount)) {
            data.rspDesc = "发票金额不能为空"
            mBs.post(data)
            return
        }

        if (invoiceAmount.toInt() * 100 > invoiceAmtLimit) {
            data.rspDesc = "发票金额不能大于可开金额"
            mBs.post(data)
            return
        }

        if (invoiceAmount.toInt() == 0) {
            data.rspDesc = "发票金额不能为0"
            mBs.post(data)
            return
        }

        if (TextUtil.isEmpty(invoiceContent)) {
            data.rspDesc = "发票内容不能为空"
            mBs.post(data)
            return
        }

        if (TextUtil.isEmpty(name)) {
            data.rspDesc = "收件人姓名不能为空"
            mBs.post(data)
            return
        }

        if (TextUtil.isEmpty(mobile)) {
            data.rspDesc = "收件人手机号不能为空"
            mBs.post(data)
            return
        }

        if (!TextUtil.isMobileNO(mobile)) {
            data.rspDesc = "请输入正确手机号"
            mBs.post(data)
            return
        }

        if (TextUtil.isEmpty(area)) {
            data.rspDesc = "收件人地区不能为空"
            mBs.post(data)
            return
        }

        if (TextUtil.isEmpty(addr)) {
            data.rspDesc = "收件人地址不能为空"
            mBs.post(data)
            return
        }

        manger.applyInvoice(usrId, invoiceType, invoiceTitle, invoiceCode, invoiceAmount.toInt() * 100, invoiceContent, name,
                mobile, area, addr, object : Callback<CommEntity> {
            override fun onResponse(call: Call<CommEntity>, response: Response<CommEntity>) {
                LogUtils.d("applyInvoice", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<CommEntity>, t: Throwable) {
                val data = CommEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun payOrder(orderNo: String, coupon: String, payType: Int) {
        manger.payOrder(orderNo, coupon, payType, object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                LogUtils.d("payOrder", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                val data = CommEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun payBack(orderNo: String, orderStatus: Int) {
        manger.payBack(orderNo, orderStatus, object : Callback<CommEntity> {
            override fun onResponse(call: Call<CommEntity>, response: Response<CommEntity>) {
                LogUtils.d("payBack", "" + response.body())
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<CommEntity>, t: Throwable) {
                val data = CommEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }


    fun qryOrder(orderNo: String) {
        manger.qryOrder(orderNo, object : Callback<QryOrderEntity> {
            override fun onResponse(call: Call<QryOrderEntity>, response: Response<QryOrderEntity>) {
                LogUtils.d("qryOrder", "" + response.body() + "")
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<QryOrderEntity>, t: Throwable) {
                val data = QryOrderEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }


    fun listOrder(usrId: Int, pageIndex: Int, pageCount: Int) {
        manger.listOrder(usrId, pageIndex, pageCount, object : Callback<ListOrderEntity> {
            override fun onResponse(call: Call<ListOrderEntity>, response: Response<ListOrderEntity>) {

                if (response.body() != null) {
                    LogUtils.d("listOrder", response.body().toString())
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<ListOrderEntity>, t: Throwable) {
                val data = ListOrderEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })

    }

    fun checkVersion(key: String) {
        manger.checkVersion(key, object : Callback<CheckVersionEntity> {
            override fun onResponse(call: Call<CheckVersionEntity>, response: Response<CheckVersionEntity>) {
                LogUtils.d("checkVersion", "" + response.body() + "")
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<CheckVersionEntity>, t: Throwable) {
                val data = CheckVersionEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }

    fun suggestion(suggestion: String) {
        manger.suggestion(suggestion, object : Callback<CommEntity> {
            override fun onFailure(call: Call<CommEntity>, t: Throwable) {
                val data = CommEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }

            override fun onResponse(call: Call<CommEntity>, response: Response<CommEntity>) {
                LogUtils.d("suggestion", "" + response.body() + "")
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }
        })
    }

    fun evaluate(orderNo: String, point: Int, evaluate: String) {
        manger.evaluate(orderNo, point, evaluate, object : Callback<CommentResultEntity> {
            override fun onResponse(call: Call<CommentResultEntity>, response: Response<CommentResultEntity>) {
                LogUtils.d("evaluate", "" + response.body() + "")
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<CommentResultEntity>, t: Throwable) {
                val data = CommentResultEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }

        })
    }

    fun qryAirport(city: String) {
        manger.qryAirport(city, object : Callback<QryAirportEntity> {
            override fun onResponse(call: Call<QryAirportEntity>, response: Response<QryAirportEntity>) {
                LogUtils.d("qryAirport", "" + response.body() + "")
                if (response.body() != null) {
                    mBs.post(response.body())
                }
            }

            override fun onFailure(call: Call<QryAirportEntity>, t: Throwable) {
                val data = QryAirportEntity()
                data.rspCode = "1000"
                data.rspDesc = "网络连接失败"
                mBs.post(data)
                t.printStackTrace()
            }
        })
    }
}