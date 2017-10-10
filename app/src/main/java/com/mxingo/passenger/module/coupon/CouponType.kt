package com.mxingo.passenger.module.order

/**
 * Created by zhouwei on 2017/8/3.
 */
enum class CouponType{
    DISCOUNT(1, "折扣券"),MIN_AMOUNT(2, "满减券");
    private var value: Int
    private  var key: String
    private constructor(value: Int, key: String){
        this.value = value
        this.key = key
    }
    companion object {
        @JvmStatic
        fun getKey(index: Int): String {
            CouponType.values().forEach {
                if (it.value == index) {
                    return it.key
                }
            }
            return ""
        }
        const val DISCOUNT_TYPE = 1
        const val MIN_AMOUNT_TYPE = 2

    }
}