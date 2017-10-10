package com.mxingo.passenger.module.order

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.baidu.mapapi.search.core.PoiInfo
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.model.FlightEntity
import com.mxingo.passenger.model.PubOrderEntity
import com.mxingo.passenger.model.QryCarLevelEntity
import com.mxingo.passenger.module.base.data.UserInfoPreferences
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.module.base.log.LogUtils
import com.mxingo.passenger.module.base.map.BaiduMapUtil
import com.mxingo.passenger.module.order.TimePicker.OnSelectDate
import com.mxingo.passenger.util.TextUtil
import com.mxingo.passenger.widget.MyProgress
import com.mxingo.passenger.widget.OnePicker
import com.mxingo.passenger.widget.ShowToast
import com.squareup.otto.Subscribe
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PubOrderActivity : BaseActivity() {
    private lateinit var tabPubOrder: TabLayout
    private lateinit var rlFlight: RelativeLayout
    private lateinit var tvFlight: TextView
    private lateinit var rlBookTime: RelativeLayout
    private lateinit var tvBookTime: TextView
    private lateinit var rlRenterDay: RelativeLayout
    private lateinit var tvRenterDay: TextView
    private lateinit var rlTakeAddress: RelativeLayout
    private lateinit var tvTakeAddress: TextView
    private lateinit var rlAirport: RelativeLayout
    private lateinit var tvAirport: TextView
    private lateinit var rlSendAddress: RelativeLayout
    private lateinit var tvSendAddress: TextView
    private lateinit var rlCarLevel: RelativeLayout
    private lateinit var tvCarLevel: TextView
    private lateinit var tvPassenger: TextView
    private lateinit var tvMobile: TextView
    private lateinit var etRemark: EditText
    private lateinit var btnPubOrder: Button


    private var orderType = 1
    private lateinit var timePicker: TimePicker

    private lateinit var renterDayPicker: OnePicker
    private lateinit var datePicker: OnePicker
    private var poiInfo: PoiInfo? = null
    private var poiInfoAirport: PoiInfo? = null
    private var carType: QryCarLevelEntity.QueryResultListEntity? = null

    private var dataEmpty = arrayListOf(0, 0, 0, 0, 0, 0)
    private var userId = 0

    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress


    companion object {
        @JvmStatic
        fun startPubOrderActivity(context: Context, userId: Int) {
            context.startActivity(Intent(context, PubOrderActivity::class.java).putExtra(Constants.USER_ID, userId))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pub_order)
        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)
        progress = MyProgress(this)
        init()
    }

    private fun init() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setToolbar(toolbar)
        val tvToolbar = findViewById(R.id.tv_toolbar_title) as TextView
        tvToolbar.text = getString(R.string.app_name)

        tabPubOrder = findViewById(R.id.tab_pub_order) as TabLayout
        rlFlight = findViewById(R.id.rl_flight) as RelativeLayout
        tvFlight = findViewById(R.id.tv_flight) as TextView
        rlBookTime = findViewById(R.id.rl_book_time) as RelativeLayout
        tvBookTime = findViewById(R.id.tv_book_time) as TextView
        rlRenterDay = findViewById(R.id.rl_renter_day) as RelativeLayout
        tvRenterDay = findViewById(R.id.tv_renter_day) as TextView
        rlTakeAddress = findViewById(R.id.rl_take_address) as RelativeLayout
        tvTakeAddress = findViewById(R.id.tv_take_address) as TextView
        rlAirport = findViewById(R.id.rl_airport) as RelativeLayout
        tvAirport = findViewById(R.id.tv_airport) as TextView
        rlSendAddress = findViewById(R.id.rl_send_address) as RelativeLayout
        tvSendAddress = findViewById(R.id.tv_send_address) as TextView
        rlCarLevel = findViewById(R.id.rl_car_level) as RelativeLayout
        tvCarLevel = findViewById(R.id.tv_car_level) as TextView
        tvPassenger = findViewById(R.id.tv_passenger) as TextView
        tvMobile = findViewById(R.id.tv_mobile) as TextView
        etRemark = findViewById(R.id.et_remark) as EditText
        btnPubOrder = findViewById(R.id.btn_pub_order) as Button

        btnPubOrder = findViewById(R.id.btn_pub_order) as Button
        btnPubOrder.setOnClickListener {
            pubOrder()
        }
        userId = intent.getIntExtra(Constants.USER_ID, 0)
        timePicker = TimePicker(this)
        initListener()
        initView(0)
        tvMobile.text = UserInfoPreferences.getInstance().mobile
    }

    private fun initListener() {
        renterDayPicker = OnePicker(this, (0..7).mapTo(arrayListOf()) {
            if (it == 0) {
                "半天"
            } else {
                "" + it + "天"
            }
        })
        val format = SimpleDateFormat("yyyy年MM月dd日")//小写的mm表示的是分钟
        var date = Date().time

        datePicker = OnePicker(this, (1..30).mapTo(arrayListOf()) {
            date +=  24 * 3600 * 1000
            format.format(date)
        })

        tabPubOrder.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                initView(tab!!.position)
            }
        })

        rlFlight.setOnClickListener {
            SearchFlightActivity.startSearchFlightActivity(this)
        }

        rlBookTime.setOnClickListener {
            if (orderType != OrderType.DAY_RENTER_TYPE) {
                timePicker.show()
            } else {
                datePicker.show()
            }
        }

        rlRenterDay.setOnClickListener {
            renterDayPicker.show()
        }

        rlTakeAddress.setOnClickListener {
            SearchAddressActivity.startSearchAddressActivity(this, BaiduMapUtil.getInstance().cityName)
        }

        rlSendAddress.setOnClickListener {
            SearchAddressActivity.startSearchAddressActivity(this, BaiduMapUtil.getInstance().cityName)
        }
        rlAirport.setOnClickListener {
            AirportWebActivity.startAirportWebActivity(this, Constants.HTML_URL + "/airport.html?city=${BaiduMapUtil.getInstance().cityName}")
        }

        rlCarLevel.setOnClickListener {
            when (orderType) {
                OrderType.DAY_RENTER_TYPE -> {
                    if (!TextUtil.isEmpty(tvBookTime.text.toString()) && poiInfo != null && !TextUtil.isEmpty(tvRenterDay.text.toString())) {
                        CarLevelActivity.startCarLevelActivity(this, 0.0, 0.0, 0.0, 0.0, orderType, tvBookTime.text.toString().replace("日", "").replace("\\D".toRegex(), "-"), poiInfo!!.city, tvRenterDay.tag.toString().toInt())
                    }
                }
                OrderType.TAKE_PLANE_TYPE -> {
                    if (poiInfo != null && poiInfoAirport != null && !TextUtil.isEmpty(tvBookTime.text.toString())) {
                        CarLevelActivity.startCarLevelActivity(this, poiInfoAirport!!.location.longitude, poiInfoAirport!!.location.latitude, poiInfo!!.location.longitude,
                                poiInfo!!.location.latitude, orderType, tvBookTime.text.toString().replace("\\D".toRegex(), ""), "", 0)
                    }
                }
                OrderType.SEND_PLANE_TYPE -> {
                    if (poiInfo != null && poiInfoAirport != null && !TextUtil.isEmpty(tvBookTime.text.toString())) {
                        CarLevelActivity.startCarLevelActivity(this, poiInfo!!.location.longitude, poiInfo!!.location.latitude, poiInfoAirport!!.location.longitude,
                                poiInfoAirport!!.location.latitude, orderType, tvBookTime.text.toString().replace("\\D".toRegex(), ""), "", 0)
                    }
                }

            }
        }

        findViewById(R.id.ll_edit).setOnClickListener {
            ChangePassengerActivity.startChangePassengerActivity(this)
        }

        timePicker.onSelectDate = object : OnSelectDate {
            override fun selectDate(date: String) {
                tvBookTime.text = date
                dataEmpty[0] = 1
                checkView()
            }

        }

        datePicker.setOnPickListener {
            tvBookTime.text = datePicker.selectedItem
            dataEmpty[0] = 1
            checkView()
        }
        renterDayPicker.setOnPickListener {
            tvRenterDay.text = renterDayPicker.selectedItem
            if (renterDayPicker.selectedIndex == 0) {
                tvRenterDay.tag = 999
            } else {
                tvRenterDay.tag = renterDayPicker.selectedIndex
            }
            dataEmpty[1] = 1
            checkView()
        }
    }


    private fun initView(position: Int) {
        when (position) {
            0 -> {//接机
                orderType = OrderType.TAKE_PLANE_TYPE
                rlFlight.visibility = View.VISIBLE
                rlRenterDay.visibility = View.GONE
                rlTakeAddress.visibility = View.GONE
                rlAirport.visibility = View.VISIBLE
                rlSendAddress.visibility = View.VISIBLE
            }
            1 -> {//送机
                orderType = OrderType.SEND_PLANE_TYPE
                rlFlight.visibility = View.GONE
                rlRenterDay.visibility = View.GONE
                rlTakeAddress.visibility = View.VISIBLE
                rlAirport.visibility = View.VISIBLE
                rlSendAddress.visibility = View.GONE
            }
            2 -> {//日租
                orderType = OrderType.DAY_RENTER_TYPE
                rlFlight.visibility = View.GONE
                rlRenterDay.visibility = View.VISIBLE
                rlTakeAddress.visibility = View.VISIBLE
                rlAirport.visibility = View.GONE
                rlSendAddress.visibility = View.GONE

            }
        }
        tvFlight.text = ""
        tvBookTime.text = ""
        tvRenterDay.text = ""
        tvSendAddress.text = ""
        tvTakeAddress.text = ""
        tvAirport.text = ""
        tvCarLevel.text = ""
        dataEmpty = arrayListOf(0, 0, 0, 0, 0, 0)
        checkView()
    }

    private fun checkView() {
        when (orderType) {
            OrderType.TAKE_PLANE_TYPE -> {//接机
                if (dataEmpty[0] + dataEmpty[3] + dataEmpty[4] + dataEmpty[5] == 4) {
                    btnPubOrder.isSelected = true
                    btnPubOrder.isClickable = true
                    LogUtils.d("TAKE_PLANE_TYPE", "OK")
                } else {
                    btnPubOrder.isSelected = false
                    btnPubOrder.isClickable = false
                    LogUtils.d("TAKE_PLANE_TYPE", "NO" + dataEmpty[0] + "," + dataEmpty[3] + "," + dataEmpty[4] + "," + dataEmpty[5])
                }
            }
            OrderType.SEND_PLANE_TYPE -> {//送机
                if (dataEmpty[0] + dataEmpty[2] + dataEmpty[3] + dataEmpty[5] == 4) {
                    btnPubOrder.isSelected = true
                    btnPubOrder.isClickable = true
                } else {
                    btnPubOrder.isSelected = false
                    btnPubOrder.isClickable = false
                }
            }
            OrderType.DAY_RENTER_TYPE -> {//日租
                if (dataEmpty[0] + dataEmpty[1] + dataEmpty[2] + dataEmpty[5] == 4) {
                    btnPubOrder.isSelected = true
                    btnPubOrder.isClickable = true
                } else {
                    btnPubOrder.isSelected = false
                    btnPubOrder.isClickable = false
                    LogUtils.d("TAKE_PLANE_TYPE", "NO" + dataEmpty[0] + "," + dataEmpty[1] + "," + dataEmpty[2] + "," + dataEmpty[5])
                }
            }
        }
    }

    private fun pubOrder() {

        progress.show()
        when (orderType) {
            OrderType.TAKE_PLANE_TYPE -> {
                val sdf = SimpleDateFormat("yyyy年MM月dd日 HH点mm分")
                val date = sdf.parse(tvBookTime.text.toString())
                presenter.pubOrder(userId, carType!!.VehicleType, tvFlight.text.toString(), tvAirport.tag.toString(), date.time, 0, tvPassenger.text.toString(),
                        tvMobile.text.toString(), orderType, (carType!!.Price * 100).toInt(), tvAirport.text.toString(), poiInfoAirport!!.location.longitude,
                        poiInfoAirport!!.location.latitude, tvSendAddress.text.toString(), poiInfo!!.location.longitude, poiInfo!!.location.latitude, etRemark.text.toString(), "", "")
            }
            OrderType.SEND_PLANE_TYPE -> {
                val sdf = SimpleDateFormat("yyyy年MM月dd日 HH点mm分")
                val date = sdf.parse(tvBookTime.text.toString())
                presenter.pubOrder(userId, carType!!.VehicleType, "", tvAirport.tag.toString(), date.time, 0, tvPassenger.text.toString(), tvMobile.text.toString(),
                        orderType, (carType!!.Price * 100).toInt(), tvTakeAddress.text.toString(), poiInfo!!.location.longitude, poiInfo!!.location.latitude,
                        tvAirport.text.toString(), poiInfoAirport!!.location.longitude, poiInfoAirport!!.location.latitude, etRemark.text.toString(), "", "")
            }
            OrderType.DAY_RENTER_TYPE -> {
                val sdf = SimpleDateFormat("yyyy年MM月dd日")
                val date = sdf.parse(tvBookTime.text.toString())
                presenter.pubOrder(userId, carType!!.VehicleType, "", "", date.time, tvRenterDay.tag.toString().toInt(), tvPassenger.text.toString(), tvMobile.text.toString(),
                        orderType, (carType!!.Price * 100).toInt(), tvTakeAddress.text.toString(), poiInfo!!.location.longitude, poiInfo!!.location.latitude,
                        "无", 0.0, 0.0, etRemark.text.toString(), carType!!.ProductId, poiInfo!!.city)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_SELECT_ADDRESS_ACTIVITY -> {
                    poiInfo = data!!.getParcelableExtra(Constants.ACTIVITY_BACK_DATA)
                    if (orderType == OrderType.TAKE_PLANE_TYPE) {
                        if (!tvSendAddress.text.isEmpty()) {
                            dataEmpty[5] = 0
                            tvCarLevel.text = ""
                        }
                        tvSendAddress.text = poiInfo!!.name
                        dataEmpty[4] = 1

                    } else {
                        if (!tvTakeAddress.text.isEmpty()) {
                            dataEmpty[5] = 0
                            tvCarLevel.text = ""
                        }
                        tvTakeAddress.text = poiInfo!!.name
                        dataEmpty[2] = 1
                    }
                    checkView()
                }
                Constants.REQUEST_AIRPORT_ACTIVITY -> {
                    if (!tvCarLevel.text.isEmpty()) {
                        dataEmpty[5] = 0
                        tvCarLevel.text = ""
                    }
                    poiInfoAirport = data!!.getParcelableExtra(Constants.ACTIVITY_BACK_DATA)
                    tvAirport.text = data.getStringExtra(Constants.ACTIVITY_BACK_AIRPORT)
                    tvAirport.tag = data.getStringExtra(Constants.ACTIVITY_BACK_AIRPORT_THREE_CODE)
                    dataEmpty[3] = 1
                    checkView()
                }
                Constants.REQUEST_CAR_LEVEL_ACTIVITY -> {
                    carType = data!!.getSerializableExtra(Constants.ACTIVITY_BACK_DATA) as QryCarLevelEntity.QueryResultListEntity
                    tvCarLevel.text = "${CarType.getKey(carType!!.VehicleType.toInt())}       ¥${carType!!.Price}/一趟"
                    dataEmpty[5] = 1
                    checkView()
                }
                Constants.REQUEST_ChANGE_PASSENGER_ACTIVITY -> {
                    tvPassenger.text = data!!.getStringExtra(Constants.ACTIVITY_PASSENGER_NAME)
                    tvMobile.text = data.getStringExtra(Constants.ACTIVITY_PASSENGER_MOBILE)
                }
                Constants.REQUEST_FLIGHT_ACTIVITY -> {
                    val flight = data!!.getSerializableExtra(Constants.ACTIVITY_BACK_DATA) as FlightEntity.AvFltModelEntity
                    tvFlight.text = flight.FlightNo
                    if (!TextUtil.isEmpty(flight.ArrTime)) {
                        tvBookTime.text = flight.ArrTime
                        dataEmpty[0] = 1
                    }
                    if (flight.Arr != null && !TextUtil.isEmpty(flight.Arr.name)) {
                        tvAirport.text = flight.Arr.name
                        tvAirport.tag = flight.Arr.code
                        dataEmpty[3] = 1
                    }
                    poiInfoAirport = data.getParcelableExtra(Constants.ACTIVITY_BACK_AIRPORT)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unregister(this)
    }

    @Subscribe
    fun loadData(any: Any) {
        progress.dismiss()
        val data = any as PubOrderEntity
        if (data.rspCode.equals("00")) {
            PayOrderActivity.startPayOrderActivity(this, data.orderNo)
            finish()
        } else {
            ShowToast.showCenter(this, data.rspDesc)
        }
    }
}
