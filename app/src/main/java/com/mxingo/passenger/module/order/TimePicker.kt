package com.mxingo.passenger.module.order

import android.app.Activity
import com.mxingo.passenger.widget.ThreePicker
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by zhouwei on 2017/8/1.
 */
class TimePicker() {
    private lateinit var timeP: ThreePicker
    private lateinit var context: Activity
    private val intervalHour = 3
    var onSelectDate: OnSelectDate? = null


    interface OnSelectDate {
        fun selectDate(date: String)
    }

    constructor(context: Activity) : this() {
        this.context = context
        initTimePicker()
    }

    private fun initTimePicker() {
        val day = arrayListOf<String>()
        val hour1 = arrayListOf<String>()
        val min1 = arrayListOf<String>()
        val hour2 = arrayListOf<String>()
        val min2 = arrayListOf<String>()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis += (intervalHour * 3600 * 1000).toLong()
        var minute = calendar.get(Calendar.MINUTE)
        if (minute > 50) {
            calendar.timeInMillis += ((20 - minute) * 60 * 1000).toLong()
            minute = calendar.get(Calendar.MINUTE)
        }
        if (minute % 10 != 0) {
            minute = minute / 10 * 10 + 10
        }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val format = SimpleDateFormat("yyyy年MM月dd日")//小写的mm表示的是分钟
        var date = Date().time
        (0..30).mapTo(day) {
            if(it != 0){
                date +=  24 * 3600 * 1000
            }
            format.format(date)
        }
        (0..23).mapTo(hour2) {
            if (it < 10) {
                "0" + it
            } else {
                it.toString()
            }
        }
        (0..5).mapTo(min2) {
            if (it * 10 < 10) {
                "0" + (it * 10)
            } else {
                (it * 10).toString()
            }
        }

        (hour..23).mapTo(hour1) {
            if (it < 10) {
                "0" + it
            } else {
                it.toString()
            }
        }
        (minute / 10..5).mapTo(min1) {
            if (it * 10 < 10) {
                "0" + (it * 10) + "分"
            } else {
                (it * 10).toString()
            }
        }

        timeP = ThreePicker(context, day, hour1, min1)
        timeP.setTitleText("时间选择")

        timeP.setOnWheelListener(object : ThreePicker.OnWheelListener {
            override fun onFirstWheeled(index: Int, item: String?) {
                if (index == 0) {
                    timeP.updateDataSecond(hour1, 0)
                    timeP.updateDataThree(min1, 0)
                } else {
                    timeP.updateDataSecond(hour2, 0)
                    timeP.updateDataThree(min2, 0)
                }
            }

            override fun onSecondWheeled(index: Int, item: String?) {
                if (index == 0 && timeP.selectedFirstIndex == 0) {
                    timeP.updateDataThree(min1, 0)
                } else {
                    timeP.updateDataThree(min2, 0)
                }
            }

            override fun onThreeWheeled(index: Int, item: String?) {
            }

        })
        timeP.setOnPickListener { _, _, _ ->
            onSelectDate!!.selectDate(timeP.selectedFirstItem + " " + timeP.selectedSecondItem +"点"+ timeP.selectedThreeItem+"分")
        }
    }

    fun show() {
        timeP.show()
    }


}