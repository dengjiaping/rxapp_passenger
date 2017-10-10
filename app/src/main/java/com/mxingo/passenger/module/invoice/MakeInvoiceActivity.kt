package com.mxingo.passenger.module.invoice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.model.CommEntity
import com.mxingo.passenger.model.QryInvoiceLimitEntity
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.module.order.AddressPicker
import com.mxingo.passenger.module.order.InvoiceType
import com.mxingo.passenger.widget.MyProgress
import com.mxingo.passenger.widget.ShowToast
import com.squareup.otto.Subscribe
import javax.inject.Inject

class MakeInvoiceActivity : BaseActivity() {

    private var userId = 0
    private lateinit var llInvoiceTitle: LinearLayout
    private lateinit var etInvoiceTitle: EditText
    private lateinit var llTaxNumber: LinearLayout
    private lateinit var etTaxNumber: EditText
    private lateinit var tvInvoiceContent: TextView
    private lateinit var etInvoiceValue: EditText
    private lateinit var etAddressee: EditText
    private lateinit var etAddresseeMobile: EditText
    private lateinit var tvArea: TextView
    private lateinit var tvInvoiceLimit: TextView
    private lateinit var etAddress: EditText

    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress
    private lateinit var rbCompany: RadioButton
    private var invoiceType = 2

    private lateinit var addressPicker: AddressPicker

    private var invoiceAmountLimit = 0

    companion object {
        @JvmStatic
        fun startRecordInvoiceActivity(activity: Activity, userId: Int) {
            activity.startActivityForResult(Intent(activity, MakeInvoiceActivity::class.java)
                    .putExtra(Constants.USER_ID, userId)
                    , Constants.REQUEST_MAKE_INVOICE_ACTIVITY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_invoice)

        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)
        progress = MyProgress(this)
        addressPicker = AddressPicker(this, "地址选择")

        userId = intent.getIntExtra(Constants.USER_ID, 0)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setToolbar(toolbar)
        val tvToolbar = findViewById(R.id.tv_toolbar_title) as TextView
        tvToolbar.text = "开票信息"

        llInvoiceTitle = findViewById(R.id.ll_invoice_title) as LinearLayout
        etInvoiceTitle = findViewById(R.id.et_invoice_title) as EditText
        llTaxNumber = findViewById(R.id.ll_tax_number) as LinearLayout
        etTaxNumber = findViewById(R.id.et_tax_number) as EditText
        tvInvoiceContent = findViewById(R.id.et_invoice_content) as TextView
        etInvoiceValue = findViewById(R.id.et_invoice_value) as EditText
        etAddressee = findViewById(R.id.et_addressee) as EditText
        tvArea = findViewById(R.id.tv_area) as TextView
        tvInvoiceLimit = findViewById(R.id.tv_invoice_limit) as TextView
        etAddresseeMobile = findViewById(R.id.et_addressee_mobile) as EditText
        etAddress = findViewById(R.id.et_address) as EditText

        rbCompany = findViewById(R.id.rb_company) as RadioButton
        rbCompany.isChecked = true
        rbCompany.setOnClickListener {
            invoiceType = InvoiceType.COMPANY_TYPE
            llTaxNumber.visibility = View.VISIBLE
            etInvoiceTitle.isEnabled = true
            etInvoiceTitle.text.clear()

        }
        findViewById(R.id.rb_person).setOnClickListener {
            invoiceType = InvoiceType.PERSON_TYPE
            etInvoiceTitle.text.clear()
            etInvoiceTitle.append("个人")
            etInvoiceTitle.isEnabled = false
            llTaxNumber.visibility = View.GONE
        }

        findViewById(R.id.ll_area).setOnClickListener {
            addressPicker.show()
        }

        findViewById(R.id.btn_apply_invoice).setOnClickListener {
            progress.show()
            presenter.applyInvoice(userId, invoiceType, etInvoiceTitle.text.toString(), etTaxNumber.text.toString()
                    , etInvoiceValue.text.toString(), invoiceAmountLimit ,tvInvoiceContent.text.toString(), etAddressee.text.toString()
                    , etAddresseeMobile.text.toString(), tvArea.text.toString(), etAddress.text.toString())
        }
        addressPicker.onSelectData = object : AddressPicker.OnSelectData {
            override fun selectCity(city: String, areaCode: String) {
            }

            override fun selectProvince(province: String, areaCode: String) {
            }

            override fun selectArea(area: String, areaCode: String) {
            }

            override fun selectData(data: String) {
                tvArea.text = data
            }
        }

        etInvoiceValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                if (editable != null) {
                    if (editable.toString().length == 2) {
                        val first = editable.first()
                        val last = editable.last()
                        if (first.toString().equals("0")) {
                            etInvoiceValue.text.clear()
                            etInvoiceValue.append(last.toString())
                            etInvoiceValue.setSelection(1)
                        }
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        progress.show()
        presenter.qryInvoiceLimit(userId)
    }

    @Subscribe
    fun loadData(any: Any) {
        progress.dismiss()
        when (any::class.java) {
            CommEntity::class.java -> {
                val comm = any as CommEntity
                if (comm.rspCode.equals("00")) {
                    RecordInvoiceActivity.startRecordInvoiceActivity(this, userId)
                } else {
                    ShowToast.showCenter(this, comm.rspDesc)
                }
            }
            QryInvoiceLimitEntity::class.java -> {
                val data = any as QryInvoiceLimitEntity
                if (data.rspCode.equals("00")) {
                    invoiceAmountLimit = data.invoiceAmount
                    tvInvoiceLimit.text = "￥${data.invoiceAmount / 100.0}"
                } else {
                    ShowToast.showCenter(this, data.rspDesc)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unregister(this)
    }
}
