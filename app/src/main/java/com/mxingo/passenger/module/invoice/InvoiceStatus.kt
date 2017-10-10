package com.mxingo.passenger.module.order

/**
 * Created by zhouwei on 2017/8/3.
 */
enum class InvoiceStatus{
    COMMIT_APPLY(0, "提交申请"), APPLY_SUC(1, "审核通过"),APPLY_FAIL(2, "审核失败"),SEND_INVOICE(3, "审核失败"),APPLY_FINISH(4, "申请完成");

    private var value: Int
    private  var key: String
    private constructor(value: Int, key: String){
        this.value = value
        this.key = key
    }
    companion object {
        @JvmStatic
        fun getKey(index: Int): String {
            InvoiceStatus.values().forEach {
                if (it.value == index) {
                    return it.key
                }
            }
            return ""
        }
        const val COMMIT_APPLY_TYPE = 0
        const val APPLY_SUC_TYPE = 1
        const val APPLY_FAIL_TYPE = 2
        const val SEND_INVOICE = 3
        const val APPLY_FINISH = 4

    }
}