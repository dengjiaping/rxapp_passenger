package com.mxingo.passenger.module.coupon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.model.ListCouponEntity
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.module.order.CouponAdapter
import com.mxingo.passenger.widget.MyFooterView
import com.mxingo.passenger.widget.MyProgress
import com.squareup.otto.Subscribe
import javax.inject.Inject

class CouponsActivity : BaseActivity() {

    private lateinit var lvCoupon: ListView
    private lateinit var llEmpty: LinearLayout
    private lateinit var adapter: CouponAdapter
    private lateinit var footView: MyFooterView

    private var userId = 0
    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress

    private var pageIndex = 0
    private var pageCount = 20

    companion object {
        @JvmStatic
        fun startCouponsActivity(context: Context, userId: Int) {
            context.startActivity(Intent(context, CouponsActivity::class.java).putExtra(Constants.USER_ID, userId))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupons)
        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)
        progress = MyProgress(this)
        userId = intent.getIntExtra(Constants.USER_ID, 0)

        initView()
        progress.show()
        presenter.listCoupon(userId, pageIndex, pageCount)
    }

    private fun initView() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setToolbar(toolbar)
        val tvToolbar = findViewById(R.id.tv_toolbar_title) as TextView
        tvToolbar.text = "用车券"

        lvCoupon = findViewById(R.id.lv_coupon) as ListView
        llEmpty = findViewById(R.id.ll_empty) as LinearLayout
        adapter = CouponAdapter(this)
        footView = MyFooterView(this)
        lvCoupon.adapter = adapter
        lvCoupon.emptyView = llEmpty
        lvCoupon.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
            }

            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (totalItemCount == 0)
                    return
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    val lastItemView = lvCoupon.getChildAt(lvCoupon.childCount - 1)
                    if (lvCoupon.bottom == lastItemView.bottom && footView.refresh) {
                        pageIndex += pageCount
                        presenter.listCoupon(userId, pageIndex, pageCount)
                    }
                }
            }

        })
    }

    @Subscribe
    fun loadData(couponData: ListCouponEntity) {
        progress.dismiss()
        if (couponData.rspCode.equals("00")) {
            adapter.addAllData(couponData.coupons)
            footView.refresh = couponData.coupons.size >= pageCount
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unregister(this)
    }
}
