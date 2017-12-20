package com.mxingo.passenger.module.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.model.CommEntity
import com.mxingo.passenger.module.base.data.UserInfoPreferences
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.util.StartUtil
import com.mxingo.passenger.widget.MyProgress
import com.mxingo.passenger.widget.ShowToast
import com.squareup.otto.Subscribe
import javax.inject.Inject

/**
 * Created by chendeqiang on 2017/11/13 14:42
 */
class SuggestionsActivity : BaseActivity() {
    private var userId = 0
    private lateinit var tabSuggestion: TabLayout
    private lateinit var llSuggestion: LinearLayout
    private lateinit var llComplain: LinearLayout
    private lateinit var btnCall: Button
    private lateinit var btnPub: Button
    private lateinit var etContent: EditText
    private lateinit var tvLength: TextView
    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress

    companion object {
        @JvmStatic
        fun startSuggestionsActivity(context: Context, userId: Int) {
            context.startActivity(Intent(context, SuggestionsActivity::class.java).putExtra(Constants.USER_ID, userId))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestions)
        userId = intent.getIntExtra(Constants.USER_ID, 0)
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
        tabSuggestion = findViewById(R.id.tab_suggestion) as TabLayout
        llSuggestion = findViewById(R.id.ll_suggestion) as LinearLayout
        llComplain = findViewById(R.id.ll_complain) as LinearLayout
        etContent = findViewById(R.id.et_content_suggestion) as EditText
        btnPub = findViewById(R.id.btn_pub_suggestion) as Button
        btnCall = findViewById(R.id.btn_call) as Button
        tvLength = findViewById(R.id.tv_length) as TextView
        initListener()
        initView(0)
    }

    private fun initListener() {

        etContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tvLength.text = (200 - etContent.text.length).toString()
                checkView()
            }
        })

        btnPub.setOnClickListener {
            progress.show()
            presenter.suggestion(etContent.text.toString())
        }

        btnCall.setOnClickListener {
            StartUtil.callMobile("4008878810", this)
        }

        tabSuggestion.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                initView(tab!!.position)
            }
        })
    }

    private fun initView(position: Int) {
        when (position) {
            0 -> {
                llSuggestion.visibility = View.VISIBLE
                llComplain.visibility = View.GONE
            }
            1 -> {
                llComplain.visibility = View.VISIBLE
                llSuggestion.visibility = View.GONE
            }
        }
    }

    private fun checkView() {
        if (!etContent.text.isEmpty()) {
            btnPub.isClickable = true
            btnPub.isSelected = true
        } else {
            btnPub.isClickable = false
            btnPub.isSelected = false
        }
    }

    @Subscribe
    fun loadData(any: Any) {
        progress.dismiss()
        val data = any as CommEntity
        if (data.rspCode.equals("00")) {
            ShowToast.showCenter(this, "评价完成，谢谢")
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unregister(this)
    }
}