package com.mxingo.passenger.module.order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener
import com.baidu.mapapi.search.poi.PoiDetailResult
import com.baidu.mapapi.search.poi.PoiIndoorResult
import com.baidu.mapapi.search.poi.PoiResult
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.model.FlightEntity
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.module.base.map.BaiduMapUtil
import com.mxingo.passenger.util.TextUtil
import com.mxingo.passenger.widget.MyProgress
import com.mxingo.passenger.widget.OnePicker
import com.mxingo.passenger.widget.ShowToast
import com.squareup.otto.Subscribe
import java.text.SimpleDateFormat
import javax.inject.Inject

class SearchFlightActivity : BaseActivity() {

    private lateinit var etFlight: EditText
    private lateinit var tvFlight: TextView
    //    private lateinit var adapter: FlightAdapter
    private lateinit var datePicker: OnePicker
    private lateinit var flightPicker: OnePicker
    private var fltModels: List<FlightEntity.AvFltModelEntity>? = null

    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress

    val sdf = SimpleDateFormat("yyyy年MM月dd日")
    val sdf2 = SimpleDateFormat("yyyy年MM月dd日 HH:mm")

    companion object {
        fun startSearchFlightActivity(activity: Activity) {
            activity.startActivityForResult(Intent(activity, SearchFlightActivity::class.java), Constants.REQUEST_FLIGHT_ACTIVITY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seach_flight)

        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)
        progress = MyProgress(this)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setToolbar(toolbar)
        val tvToolbar = findViewById(R.id.tv_toolbar_title) as TextView
        tvToolbar.text = "航班信息"

        etFlight = findViewById(R.id.et_flight) as EditText
        tvFlight = findViewById(R.id.tv_flight) as TextView
//        adapter = FlightAdapter(this)

        datePicker = OnePicker(this, (0..6).mapTo(arrayListOf()) { TextUtil.getFormatString(System.currentTimeMillis() + it * 24 * 3600 * 1000, "yyyy年MM月dd日") })
        datePicker.setTitleText("当地起飞时间")
        flightPicker = OnePicker(this, arrayListOf())
        flightPicker.setTitleText("选择终点")
        etFlight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length > 2) {
//                    adapter.addAllData(arrayListOf(p0.toString()))
                    tvFlight.text = p0.toString()
                    tvFlight.visibility = View.VISIBLE
                } else {
                    tvFlight.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        tvFlight.setOnClickListener {
            etFlight.text.clear()
            etFlight.text.append(tvFlight.text.toString())
//            adapter.clear()
            datePicker.show()
        }

        datePicker.setOnPickListener {
            val time = sdf.parse(datePicker.selectedItem).time
            progress.show()
            presenter.qryFlightiInfo(etFlight.text.toString().toUpperCase(), time)
        }

        flightPicker.setOnPickListener {
            searAddress(fltModels!![flightPicker.selectedIndex])
        }
    }

    @Subscribe
    fun loadData(data: FlightEntity) {
        progress.dismiss()
        if (data.rspCode.equals("00")) {
            fltModels = data.AvFltModel
            if (fltModels != null && fltModels!!.size >= 2) {
                val list = arrayListOf<String>()
                fltModels!!.forEach {
                    it.FlightNo = data.FlightNo
                    list.add("${it.DepCityName}  - ${it.ArrCityName} ${it.DepTime} - ${it.ArrTime}")
                }
                flightPicker.updateData(list, 0)
                flightPicker.show()
            } else if (fltModels != null && !fltModels!!.isEmpty()) {
                fltModels!![0].FlightNo = data.FlightNo
                searAddress(fltModels!![0])
            } else {
                ShowToast.showCenter(this, "数据查询失败")
                val flight = FlightEntity.AvFltModelEntity()
                flight.FlightNo = etFlight.text.toString()
                setResult(Activity.RESULT_OK, Intent().putExtra(Constants.ACTIVITY_BACK_DATA, flight))
                finish()
            }
        } else {
            ShowToast.showCenter(this, data.rspDesc)
            val flight = FlightEntity.AvFltModelEntity()
            flight.FlightNo = etFlight.text.toString()
            setResult(Activity.RESULT_OK, Intent().putExtra(Constants.ACTIVITY_BACK_DATA, flight))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unregister(this)
        BaiduMapUtil.getInstance().poiSearchDestroy()
    }

    private fun searAddress(avFlight: FlightEntity.AvFltModelEntity) {
        val intent = Intent()
        avFlight.ArrTime = TextUtil.getFormatString(sdf2.parse(datePicker.selectedItem + " " + avFlight.ArrTime).time, "yyyy年MM月dd日 HH点mm分")
        intent.putExtra(Constants.ACTIVITY_BACK_DATA, avFlight)
        try {
            BaiduMapUtil.getInstance().getPoiByPoiSearch("中国", avFlight.Arr.name, 0, object : OnGetPoiSearchResultListener {
                override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {
                }

                override fun onGetPoiResult(p0: PoiResult?) {
                    if (p0 != null && p0.allPoi != null && !p0.allPoi.isEmpty()) {
                        intent.putExtra(Constants.ACTIVITY_BACK_AIRPORT, p0.allPoi[0])
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

                override fun onGetPoiDetailResult(p0: PoiDetailResult?) {
                }
            })
        } catch (e: Exception) {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
