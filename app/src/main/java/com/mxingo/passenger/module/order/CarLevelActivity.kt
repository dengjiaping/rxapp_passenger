package com.mxingo.passenger.module.order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.ListView
import android.widget.TextView
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.model.QryCarLevelEntity
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.util.TextUtil
import com.mxingo.passenger.widget.MyProgress
import com.mxingo.passenger.widget.ShowToast
import com.squareup.otto.Subscribe
import javax.inject.Inject

class CarLevelActivity : BaseActivity() {
    private var orderType: Int = 0
    private var startLon: Double = 0.0
    private var startLat: Double = 0.0
    private var endLon: Double = 0.0
    private var endLat: Double = 0.0

    private lateinit var date: String
    private lateinit var city: String
    private var bookDays: Int = 0

    private lateinit var lvCarLevel: ListView
    private lateinit var adapter: CarLevelAdapter

    @Inject
    lateinit var presenter: MyPresenter

    private lateinit var progress: MyProgress

    companion object {
        @JvmStatic
        fun startCarLevelActivity(activity: Activity, startLon: Double, startLat: Double, endLon: Double, endLat: Double,
                                  orderType: Int, date: String, city: String, bookDays: Int) {
            activity.startActivityForResult(Intent(activity, CarLevelActivity::class.java)
                    .putExtra(Constants.ACTIVITY_ORDER_TYPE, orderType)
                    .putExtra(Constants.ACTIVITY_START_LON, startLon)
                    .putExtra(Constants.ACTIVITY_START_LAT, startLat)
                    .putExtra(Constants.ACTIVITY_END_LON, endLon)
                    .putExtra(Constants.ACTIVITY_END_Lat, endLat)
                    .putExtra(Constants.ACTIVITY_DATE, date)
                    .putExtra(Constants.ACTIVITY_CITY, city)
                    .putExtra(Constants.ACTIVITY_BOOK_DAY, bookDays)
                    , Constants.REQUEST_CAR_LEVEL_ACTIVITY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_level)
        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)
        setToolbar(toolbar = findViewById(R.id.toolbar) as Toolbar)
        (findViewById(R.id.tv_toolbar_title) as TextView).text = "车型选择"

        lvCarLevel = findViewById(R.id.lv_car_level) as ListView
        adapter = CarLevelAdapter(this)
        lvCarLevel.adapter = adapter

        lvCarLevel.setOnItemClickListener { _, _, i, _ ->
            setResult(Activity.RESULT_OK, Intent().putExtra(Constants.ACTIVITY_BACK_DATA, adapter.getItem(i) as QryCarLevelEntity.QueryResultListEntity))
            finish()
        }

        orderType = intent.getIntExtra(Constants.ACTIVITY_ORDER_TYPE, 0)
        startLat = intent.getDoubleExtra(Constants.ACTIVITY_START_LAT, 0.0)
        startLon = intent.getDoubleExtra(Constants.ACTIVITY_START_LON, 0.0)
        endLon = intent.getDoubleExtra(Constants.ACTIVITY_END_LON, 0.0)
        endLat = intent.getDoubleExtra(Constants.ACTIVITY_END_Lat, 0.0)
        date = intent.getStringExtra(Constants.ACTIVITY_DATE)
        city = intent.getStringExtra(Constants.ACTIVITY_CITY)
        bookDays = intent.getIntExtra(Constants.ACTIVITY_BOOK_DAY, 0)

        progress = MyProgress(this)
        progress.show()
        if (orderType == OrderType.DAY_RENTER_TYPE) {
            presenter.qryCarLevelDayRenter(city, bookDays, orderType, date)
        } else {
            presenter.qryCarLevel(startLon, startLat, endLon, endLat, orderType, date)
        }
    }

    @Subscribe
    fun loadData(any: Any) {
        progress.dismiss()
        val data = any as QryCarLevelEntity
        if (!TextUtil.isEmpty(data.MsgCode) && data.MsgCode.equals("OK") && data.rspCode.equals("00")) run {
            adapter.addAllData(datas = data.QueryResultList)
        } else if (!TextUtil.isEmpty(data.MsgCode) && !data.MsgCode.equals("OK") && !TextUtil.isEmpty(data.Message)) {
            ShowToast.showCenter(this, data.Message)
        } else if (!data.rspCode.equals("00")) {
            ShowToast.showCenter(this, data.rspDesc)
        }else{
            ShowToast.showCenter(this, "无可用车辆")
        }
    }
}
