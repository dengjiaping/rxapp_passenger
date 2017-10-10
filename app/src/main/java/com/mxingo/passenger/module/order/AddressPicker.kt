package com.mxingo.passenger.module.order

import android.app.Activity
import com.mxingo.passenger.model.AddressEntity
import com.mxingo.passenger.util.TextUtil
import com.mxingo.passenger.widget.ThreePicker

/**
 * Created by zhouwei on 2017/8/1.
 */
class AddressPicker() {
    lateinit var addressP: ThreePicker
    private lateinit var context: Activity
    private lateinit var title: String
    var province = arrayListOf<String>()
    var city = arrayListOf<String>()
    var area = arrayListOf<String>()
    var addresses = arrayListOf<AddressEntity>()
    var onSelectData: OnSelectData? = null


    interface OnSelectData {
        fun selectData(data: String)
        fun selectCity(city: String, areaCode: String)
        fun selectProvince(province: String, areaCode: String)
        fun selectArea(area: String, areaCode: String)
    }

    constructor(context: Activity, title: String) : this() {
        this.context = context
        this.title = title
        initTimePicker()
    }

    private fun initTimePicker() {
        addresses.addAll(TextUtil.getCity(context))
        addressP = ThreePicker(context, province, city, area)
        addressP.setTitleText(title)
        initProvince()
        initCity(0)
        initArea(0, 0)
        addressP.setOnWheelListener(object : ThreePicker.OnWheelListener {
            override fun onFirstWheeled(index: Int, item: String?) {
                initCity(index)
                initArea(index, 0)
                addressP.updateDataSecond(city, 0)
                addressP.updateDataThree(area, 0)

            }

            override fun onSecondWheeled(index: Int, item: String?) {
                initArea(addressP.selectedFirstIndex, index)
                addressP.updateDataThree(area, 0)
            }

            override fun onThreeWheeled(index: Int, item: String?) {
            }

        })
        addressP.setOnPickListener { _, _, _ ->
            onSelectData!!.selectData(addressP.selectedFirstItem + addressP.selectedSecondItem + addressP.selectedThreeItem)
            onSelectData!!.selectProvince(addressP.selectedFirstItem, province[addressP.selectedFirstIndex])
            onSelectData!!.selectCity(addressP.selectedSecondItem, city[addressP.selectedSecondIndex])
            onSelectData!!.selectArea(addressP.selectedThreeItem, area[addressP.selectedThreeIndex])
        }
    }

    private fun initProvince() {
        addresses.forEach {
            province.add(it.area_name)
        }
    }

    private fun initCity(index: Int) {
        city.clear()
        addresses[index].sub.forEach {
            city.add(it.area_name)
        }
    }

    private fun initArea(index: Int, index2: Int) {
        area.clear()
        addresses[index].sub[index2].sub.forEach {
            area.add(it.area_name)
        }
    }

    fun show() {
        addressP.show()
    }

    fun setThreeViewVisibility(visibility: Int) {
        this.addressP.setThreeViewVisibility(visibility)
    }
}