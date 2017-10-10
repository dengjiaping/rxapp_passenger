package com.mxingo.passenger.module.coupon

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.model.CouponEntity
import com.mxingo.passenger.model.ListCouponEntity
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.module.order.CouponAdapter
import com.mxingo.passenger.widget.MyProgress
import com.mxingo.passenger.widget.ShowToast
import com.squareup.otto.Subscribe
import javax.inject.Inject

class UseCouponActivity : BaseActivity() {

    private lateinit var lvCoupon: ListView
    private lateinit var llEmpty: LinearLayout
    private lateinit var adapter: CouponAdapter

    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress
    private var userId = 0
    private lateinit var orderNo: String

    companion object {
        @JvmStatic
        fun startUseCouponActivity(activity: Activity, usrId: Int, orderNo: String) {
            activity.startActivityForResult(Intent(activity, UseCouponActivity::class.java)
                    .putExtra(Constants.USER_ID, usrId)
                    .putExtra(Constants.ORDER_NO, orderNo), Constants.REQUEST_USE_COUPON_ACTIVITY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_use_coupon)
        progress = MyProgress(this)
        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)
        userId = intent.getIntExtra(Constants.USER_ID, 0)
        orderNo = intent.getStringExtra(Constants.ORDER_NO)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setToolbar(toolbar)
        val tvToolbar = findViewById(R.id.tv_toolbar_title) as TextView
        tvToolbar.text = "优惠券选择"

        lvCoupon = findViewById(R.id.lv_coupon) as ListView
        llEmpty = findViewById(R.id.ll_empty) as LinearLayout
        adapter = CouponAdapter(this)
        lvCoupon.adapter = adapter
        lvCoupon.emptyView = llEmpty

        lvCoupon.setOnItemClickListener { _, _, i, _ ->
            setResult(Activity.RESULT_OK,Intent().putExtra(Constants.ACTIVITY_BACK_DATA,adapter.getItem(i) as CouponEntity))
            finish()
        }
        progress.show()
        presenter.listCoupon4Order(userId, orderNo)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unregister(this)
    }

    @Subscribe
    fun loadData(listCouponEntity: ListCouponEntity) {
        progress.dismiss()
        if (listCouponEntity.rspCode.equals("00")) {
            adapter.addAllData(listCouponEntity.coupons)
        } else {
            ShowToast.showCenter(this, listCouponEntity.rspDesc)
        }
    }
}
