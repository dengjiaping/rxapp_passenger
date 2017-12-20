package com.mxingo.passenger.module.order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener
import com.baidu.mapapi.search.poi.PoiDetailResult
import com.baidu.mapapi.search.poi.PoiIndoorResult
import com.baidu.mapapi.search.poi.PoiResult
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.model.AirportEntity
import com.mxingo.passenger.model.QryAirportEntity
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.module.base.log.LogUtils
import com.mxingo.passenger.module.base.map.BaiduMapUtil
import com.mxingo.passenger.widget.MyProgress
import com.mxingo.passenger.widget.ShowToast
import com.squareup.otto.Subscribe
import javax.inject.Inject

/**
 * Created by chendeqiang on 2017/11/30 10:56
 */
class AirportActivity : BaseActivity() {
    private lateinit var tvCityName: TextView
    private lateinit var etAddress: EditText
    private lateinit var lvAirport: ListView
    private lateinit var adapter: AirportAdapter
    private lateinit var addressPicker: AddressPicker
    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress

    companion object {
        @JvmStatic
        fun startAirportActivity(context: Activity, cityName: String) {
            context.startActivityForResult(Intent(context, AirportActivity::class.java).putExtra(Constants.ACTIVITY_DATA, cityName), Constants.REQUEST_AIRPORT_ACTIVITY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_address)
        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)
        progress = MyProgress(this)
        tvCityName = findViewById(R.id.tv_select_city) as TextView
        etAddress = findViewById(R.id.et_address) as EditText
        lvAirport = findViewById(R.id.lv_address) as ListView
        findViewById(R.id.tv_cancel).setOnClickListener {
            finish()
        }
        findViewById(R.id.img_cancel).setOnClickListener {
            etAddress.text.clear()
        }
        tvCityName.text = intent.getStringExtra(Constants.ACTIVITY_DATA)
        adapter = AirportAdapter(this)
        lvAirport.adapter = adapter
        addressPicker = AddressPicker(this, "航站选择")
        addressPicker.onSelectData = object : AddressPicker.OnSelectData {
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
        }

        lvAirport.setOnItemClickListener { _, _, i, _ ->
            if (adapter.getItem(i)::class.java == AirportEntity::class.java) {
                var portInfo = adapter.getItem(i) as AirportEntity
                BaiduMapUtil.getInstance().getPoiByPoiSearch(tvCityName.text.toString(), portInfo.name, 0, object : OnGetPoiSearchResultListener {
                    override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {
                    }

                    override fun onGetPoiResult(p0: PoiResult?) {
                        if (p0 != null && p0.allPoi != null && !p0.allPoi.isEmpty()) {
                            LogUtils.d("data2", portInfo.name + "," + p0.allPoi[0].address + "," + p0.allPoi[0].city)
                            setResult(Activity.RESULT_OK, Intent()
                                    .putExtra(Constants.ACTIVITY_BACK_AIRPORT, portInfo.name)
                                    .putExtra(Constants.ACTIVITY_BACK_AIRPORT_THREE_CODE, portInfo.code)
                                    .putExtra(Constants.ACTIVITY_BACK_DATA, p0.allPoi[0]))
                            finish()
                        } else {
                            finish()
                        }
                    }

                    override fun onGetPoiDetailResult(p0: PoiDetailResult?) {
                    }
                })
            }
        }

        tvCityName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                presenter.qryAirport(tvCityName.text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        etAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(p0)) {
                    presenter.qryAirport(etAddress.text.toString() + "市")
                }
            }
        })
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

    override fun onStart() {
        super.onStart()
        progress.show()
        presenter.qryAirport(tvCityName.text.toString())
    }

    @Subscribe
    fun loadData(any: Any) {
        progress.dismiss()
        val data = any as QryAirportEntity
        if (data.rspCode.equals("00")) {
            adapter.addAllData(data.data)
        } else {
            ShowToast.showCenter(this, data.rspDesc)
        }
    }
}