package com.mxingo.passenger.module.invoice

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
import com.mxingo.passenger.model.QryInvoiceEnitity
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.module.base.log.LogUtils
import com.mxingo.passenger.module.order.InvoiceAdapter
import com.mxingo.passenger.widget.MyFooterView
import com.mxingo.passenger.widget.MyProgress
import javax.inject.Inject

class RecordInvoiceActivity : BaseActivity() {

    private var userId = 0
    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress
    private var pageIndex = 0
    private val pageCount = 20
    private lateinit var footView: MyFooterView
    private lateinit var llEmpty: LinearLayout
    private lateinit var lvInvoice: ListView
    private lateinit var adapter: InvoiceAdapter

    companion object {
        @JvmStatic
        fun startRecordInvoiceActivity(context: Context, userId: Int) {
            context.startActivity(Intent(context, RecordInvoiceActivity::class.java)
                    .putExtra(Constants.USER_ID, userId))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_invoice)

        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)
        progress = MyProgress(this)

        userId = intent.getIntExtra(Constants.USER_ID, 0)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setToolbar(toolbar)
        val tvToolbar = findViewById(R.id.tv_toolbar_title) as TextView
        tvToolbar.text = "开票记录"
        lvInvoice = findViewById(R.id.lv_invoice) as ListView
        llEmpty = findViewById(R.id.ll_empty) as LinearLayout

        footView = MyFooterView(this)
        adapter = InvoiceAdapter(this)
        lvInvoice.adapter = adapter
        lvInvoice.addFooterView(footView)
        lvInvoice.emptyView = llEmpty

        lvInvoice.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (totalItemCount == 0)
                    return
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    val lastItemView = lvInvoice.getChildAt(lvInvoice.childCount - 1)
                    LogUtils.d("bottom", "${lvInvoice.bottom},${lastItemView.bottom}")
                    if (lvInvoice.bottom == lastItemView.bottom && footView.refresh) {
                        pageIndex += pageCount
                        presenter.qryInvoice(userId, pageIndex, pageCount)
                    }
                }
            }

            override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
            }

        })

        progress.show()
        presenter.qryInvoice(userId, pageIndex, pageCount)
    }

    @com.squareup.otto.Subscribe
    fun loadData(any: Any) {
        progress.dismiss()
        val data = any as QryInvoiceEnitity
        if (data.rspCode.equals("00")) {
            adapter.addAll(data.invoices)
            footView.refresh = data.invoices.size >= pageCount
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unregister(this)
    }

}
