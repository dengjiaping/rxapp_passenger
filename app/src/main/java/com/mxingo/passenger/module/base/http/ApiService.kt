package com.mxingo.passenger.module.base.http

import com.mxingo.passenger.model.*
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import java.util.*

/**
 * Created by zhouwei on 2017/6/22.
 */
interface ApiService {

    @FormUrlEncoded
    @POST(ApiConstants.getVcode)
    fun getVcode(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<VCodeEntity>

    @FormUrlEncoded
    @POST(ApiConstants.login)
    fun login(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<LoginEntity>

    @FormUrlEncoded
    @POST(ApiConstants.reg)
    fun reg(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<RegEntity>

    @FormUrlEncoded
    @POST(ApiConstants.logout)
    fun logout(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<CommEntity>

    @FormUrlEncoded
    @POST(ApiConstants.qryFlightiInfo)
    fun qryFlightiInfo(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<FlightEntity>


    @FormUrlEncoded
    @POST(ApiConstants.qryCarLevel)
    fun qryCarLevel(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<QryCarLevelEntity>

    @FormUrlEncoded
    @POST(ApiConstants.cancelOrder)
    fun cancelOrder(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<CommEntity>

    @FormUrlEncoded
    @POST(ApiConstants.pubOrder)
    fun pubOrder(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<PubOrderEntity>

    @FormUrlEncoded
    @POST(ApiConstants.listCoupon)
    fun listCoupon(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<ListCouponEntity>

    @FormUrlEncoded
    @POST(ApiConstants.listCoupon4Order)
    fun listCoupon4Order(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<ListCouponEntity>

    @FormUrlEncoded
    @POST(ApiConstants.qryInvoice)
    fun qryInvoice(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<QryInvoiceEnitity>

    @FormUrlEncoded
    @POST(ApiConstants.qryInvoiceLimit)
    fun qryInvoiceLimit(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<QryInvoiceLimitEntity>

    @FormUrlEncoded
    @POST(ApiConstants.applyInvoice)
    fun applyInvoice(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<CommEntity>

    @FormUrlEncoded
    @POST(ApiConstants.payOrder)
    fun payOrder(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<String>

    @FormUrlEncoded
    @POST(ApiConstants.payBack)
    fun payBack(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<CommEntity>

    @FormUrlEncoded
    @POST(ApiConstants.qryOrder)
    fun qryOrder(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<QryOrderEntity>

    @FormUrlEncoded
    @POST(ApiConstants.getInfo)
    fun getInfo(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<String>

    @FormUrlEncoded
    @POST(ApiConstants.listOrder)
    fun listOrder(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<ListOrderEntity>

    @FormUrlEncoded
    @POST(ApiConstants.checkVersion)
    fun checkVersion(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<CheckVersionEntity>

    @FormUrlEncoded
    @POST(ApiConstants.suggestions)
    fun suggestion(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<CommEntity>

    @FormUrlEncoded
    @POST(ApiConstants.evaluate)
    fun evaluate(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<CommentResultEntity>

    @FormUrlEncoded
    @POST(ApiConstants.qryAirport)
    fun qryAirport(@FieldMap map: TreeMap<String, Any>, @HeaderMap headers: Map<String, String>): Call<QryAirportEntity>
}
