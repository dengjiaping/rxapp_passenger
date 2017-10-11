package com.mxingo.passenger.module.order

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.model.ListOrderEntity
import com.mxingo.passenger.model.OrderEntity
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.widget.MyFooterView
import com.mxingo.passenger.widget.MyProgress
import com.mxingo.passenger.widget.ShowToast
import com.squareup.otto.Subscribe
import javax.inject.Inject

class MyTripsActivity : BaseActivity() {

    private lateinit var lvTrip: ListView
    private lateinit var llEmpty: LinearLayout
    private lateinit var adapter: OrderAdapter
    private lateinit var srlRefresh: SwipeRefreshLayout
    private lateinit var footView: MyFooterView

    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress
    private var userId = 0
    private val pageCount = 20
    private var pageIndex = 0

    companion object {
        @JvmStatic
        fun startMyTripsActivity(context: Context, userId: Int) {
            context.startActivity(Intent(context, MyTripsActivity::class.java).putExtra(Constants.USER_ID, userId))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trips)
        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)
        userId = intent.getIntExtra(Constants.USER_ID, 0)
        progress = MyProgress(this)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setToolbar(toolbar)
        val tvToolbar = findViewById(R.id.tv_toolbar_title) as TextView
        tvToolbar.text = "我的行程"

        srlRefresh = findViewById(R.id.srl_refresh) as SwipeRefreshLayout
        lvTrip = findViewById(R.id.lv_trip) as ListView
        llEmpty = findViewById(R.id.ll_empty) as LinearLayout

        adapter = OrderAdapter(this)
        footView = MyFooterView(this)
        lvTrip.emptyView = llEmpty
        lvTrip.adapter = adapter
        lvTrip.addFooterView(footView)

        srlRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorButtonBg))
        srlRefresh.setOnRefreshListener {
            pageIndex = 0
            adapter.clear()
            presenter.listOrder(userId, pageIndex, pageCount)
        }

        lvTrip.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (totalItemCount == 0)
                    return
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    val lastItemView = lvTrip.getChildAt(lvTrip.childCount - 1)
                    if (lvTrip.bottom == lastItemView.bottom) {
                        if (footView.refresh) {
                            pageIndex += pageCount
                            presenter.listOrder(userId, pageIndex, pageCount)
                        }
                    }
                }
            }

            override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
            }

        })

        lvTrip.setOnItemClickListener { _, _, i, _ ->
            if (lvTrip.getChildAt(i) != footView) {
                val item = adapter.getItem(i) as OrderEntity
                if (item.orderStatus in OrderStatus.PUBORDER_TYPE..OrderStatus.FINISH_ORDER_TYPE || item.orderStatus == OrderStatus.REFUND_TYPE) {
                    OrderInfoActivity.startOrderInfoActivity(this, item.orderNo)
                } else {
                    PayOrderActivity.startPayOrderActivity(this, item.orderNo)
                }
            }
        }

        progress.show()
        presenter.listOrder(userId, pageIndex, pageCount)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == Constants.REQUEST_ORDER_INFO_ACTIVITY || requestCode == Constants.REQUEST_PAY_ORDER_ACTIVITY) && resultCode == Activity.RESULT_OK) {
            progress.show()
            adapter.clear()
            pageIndex = 0
            presenter.listOrder(userId, pageIndex, pageCount)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unregister(this)
    }

    @Subscribe
    fun loadData(listOrder: ListOrderEntity) {
        progress.dismiss()
        srlRefresh.isRefreshing = false
        if (listOrder.rspCode.equals("00")) {
            if (pageIndex == 0) {
                adapter.addAll(listOrder.waitPayOrders)
            }
            adapter.addAll(listOrder.orders)
            footView.refresh = listOrder.orders.size >= pageCount
        } else {
            ShowToast.showCenter(this, listOrder.rspDesc)
        }
    }
}
