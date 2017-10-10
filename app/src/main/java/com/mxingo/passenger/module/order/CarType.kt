package com.mxingo.passenger.module.order

/**
 * Created by zhouwei on 2017/8/3.
 */
enum class CarType {
    ECONOMIC(1, "经济型", 4), COMFORT(2, "舒适型", 4), BUSINESS(5, "商务型", 6), BUICK(911, "别克GL8", 7), DELUXE_CAR(3, "豪华型", 4), LUXURY(4, "奢华型", 4),
    DELUXE_BUSINESS(18, "豪华商务型", 4), EIGHT_MINIBUSES(20, "8座小巴", 8), TEN_MINIBUSES(14, "10座小巴", 10), FIFTEEN_MINIBUSES(21, "15座中巴", 15),
    TWENTY_BUSINESS(15, "20座中巴", 20), THIRTY_FIVE_BUSINESS(16, "35座大巴", 35), FORTY_BUSINESS(24, "40座大巴", 40), FORTY_FIVE_BUSINESS(17, "45座大巴", 45),
    FIFTY_BUSINESS(500, "50座大巴", 50), FIFTY_TWO_BUSINESS(26, "52座大巴", 52);

    private var value: Int
    private var key: String
    private var seat: Int

    private constructor(value: Int, key: String, seat: Int) {
        this.value = value
        this.key = key
        this.seat = seat
    }

    companion object {
        @JvmStatic
        fun getKey(index: Int): String {
            CarType.values().forEach {
                if (it.value == index) {
                    return it.key
                }
            }
            return ""
        }

        @JvmStatic
        fun getSeat(index: Int): Int {
            CarType.values().forEach {
                if (it.value == index) {
                    return it.seat
                }
            }
            return 0
        }

        const val ECONOMIC_TYPE = 1
        const val COMFORT_TYPE = 2
        const val BUSINESS_TYPE = 3
        const val BUICK_TYPE = 4
        const val DELUXE_CAR_TYPE = 5
        const val LUXURY_TYPE = 6
        const val DELUXE_BUSINESS_TYPE = 7
        const val EIGHT_MINIBUSES_TYPE = 20


        const val TEN_MINIBUSES_TYPE = 30
        const val FIFTEEN_MINIBUSES_TYPE = 40
        const val TWENTY_BUSINESS_TYPE = 50
        const val THIRTY_FIVE_BUSINESS_TYPE = 80
        const val FORTY_BUSINESS_TYPE = 90
        const val FORTY_FIVE_BUSINESS_TYPE = 100
        const val FIFTY_BUSINESS_TYPE = 110
        const val FIFTY_TWO_BUSINESS_TYPE = 120

    }
}