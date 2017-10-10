package com.mxingo.passenger.module.order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener
import com.baidu.mapapi.search.poi.PoiDetailResult
import com.baidu.mapapi.search.poi.PoiIndoorResult
import com.baidu.mapapi.search.poi.PoiResult
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.module.base.map.BaiduMapUtil

class SearchAddressActivity : BaseActivity() {
    private lateinit var tvCityName: TextView
    private lateinit var etAddress: EditText
    private lateinit var lvAddress: ListView
    private lateinit var adapter: AddressAdapter
    private lateinit var addressPicker:AddressPicker
    private val listener: OnGetPoiSearchResultListener = object : OnGetPoiSearchResultListener {
        override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {
        }

        override fun onGetPoiResult(p0: PoiResult?) {
            if (p0 != null && p0.allPoi != null)
                adapter.addAllData(p0.allPoi)
        }

        override fun onGetPoiDetailResult(p0: PoiDetailResult?) {

        }
    }

    companion object {
        @JvmStatic
        fun startSearchAddressActivity(context: Activity, cityName: String) {
            context.startActivityForResult(Intent(context, SearchAddressActivity::class.java).putExtra(Constants.ACTIVITY_DATA, cityName), Constants.REQUEST_SELECT_ADDRESS_ACTIVITY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_address)
        tvCityName = findViewById(R.id.tv_select_city) as TextView
        etAddress = findViewById(R.id.et_address) as EditText
        lvAddress = findViewById(R.id.lv_address) as ListView
        findViewById(R.id.tv_cancel).setOnClickListener {
            finish()
        }
        findViewById(R.id.img_cancel).setOnClickListener {
            etAddress.text.clear()
        }

        tvCityName.text = intent.getStringExtra(Constants.ACTIVITY_DATA)
        adapter = AddressAdapter(this)
        lvAddress.adapter = adapter
        etAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                BaiduMapUtil.getInstance().getPoiByPoiSearch(tvCityName.text.toString(), p0.toString(), 0, listener)

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        lvAddress.setOnItemClickListener { _, _, i, _ ->
            if (adapter.getItem(i)::class.java == PoiInfo::class.java) {
                val poiInfo = adapter.getItem(i)
                setResult(Activity.RESULT_OK, Intent()
                        .putExtra(Constants.ACTIVITY_BACK_DATA, poiInfo as Parcelable))

                finish()

            }
        }

        addressPicker = AddressPicker(this,"城市选择")

        addressPicker.onSelectData = object :AddressPicker.OnSelectData{
            override fun selectCity(city: String, areaCode: String) {
                tvCityName.text = city
            }

            override fun selectProvince(province: String, areaCode: String) {
            }

            override fun selectArea(area: String, areaCode: String) {
            }

            override fun selectData(data: String) {
            }



        }
        findViewById(R.id.tv_select_city).setOnClickListener {
            addressPicker.show()
            addressPicker.setThreeViewVisibility(View.GONE)
//            AirportWebActivity.startAirportWebActivity(this, Constants.HTML_URL + "/city.html?city=${BaiduMapUtil.getInstance().cityName}&type=and")

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_AIRPORT_ACTIVITY && resultCode == Activity.RESULT_OK) {
            tvCityName.text = data!!.getStringExtra(Constants.ACTIVITY_BACK_AIRPORT)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BaiduMapUtil.getInstance().poiSearchDestroy()
    }


}
