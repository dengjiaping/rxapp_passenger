package com.mxingo.passenger.module.order

/**
 * Created by zhouwei on 2017/8/3.
 */
enum class InvoiceType{
    PERSON(1, "个人"), COMPANY(2, "单位");

    private var value: Int
    private  var key: String
    private constructor(value: Int, key: String){
        this.value = value
        this.key = key
    }
    companion object {
        @JvmStatic
        fun getKey(index: Int): String {
            InvoiceType.values().forEach {
                if (it.value == index) {
                    return it.key
                }
            }
            return ""
        }
        const val PERSON_TYPE = 1
        const val COMPANY_TYPE = 2

    }
}