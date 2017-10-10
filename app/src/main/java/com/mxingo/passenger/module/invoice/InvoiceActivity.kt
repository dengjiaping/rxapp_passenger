package com.mxingo.passenger.module.invoice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.model.QryInvoiceLimitEntity
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.widget.MyProgress
import com.mxingo.passenger.widget.ShowToast
import com.squareup.otto.Subscribe
import javax.inject.Inject

class InvoiceActivity : BaseActivity() {
    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress
    private var userId = 0
    private lateinit var tvInvoice: TextView


    companion object {
        @JvmStatic
        fun startInvoiceActivity(context: Context, userId: Int) {
            context.startActivity(Intent(context, InvoiceActivity::class.java).putExtra(Constants.USER_ID, userId))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)
        userId = intent.getIntExtra(Constants.USER_ID, 0)

        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)
        progress = MyProgress(this)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setToolbar(toolbar)
        val tvToolbar = findViewById(R.id.tv_toolbar_title) as TextView
        tvToolbar.text = "发票管理"

        tvInvoice = findViewById(R.id.tv_invoice) as TextView
        findViewById(R.id.ll_make_out_invoice).setOnClickListener {
            MakeInvoiceActivity.startRecordInvoiceActivity(this, userId)
        }
        findViewById(R.id.ll_invoice_record).setOnClickListener {
            RecordInvoiceActivity.startRecordInvoiceActivity(this, userId)
        }

        progress.show()
        presenter.qryInvoiceLimit(userId)
    }

    @Subscribe
    fun loadData(any: Any) {
        progress.dismiss()
        val data = any as QryInvoiceLimitEntity
        if (data.rspCode.equals("00")) {
            tvInvoice.text = "￥${data.invoiceAmount / 100.0}"
        } else {
            tvInvoice.text = "￥0"
            ShowToast.showCenter(this, data.rspDesc)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unregister(this)
    }
}
