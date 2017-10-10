package com.mxingo.passenger.module.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.model.LoginEntity
import com.mxingo.passenger.model.RegEntity
import com.mxingo.passenger.model.VCodeEntity
import com.mxingo.passenger.module.WebViewActivity
import com.mxingo.passenger.module.base.data.UserInfoPreferences
import com.mxingo.passenger.module.base.http.ComponentHolder
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.module.base.log.LogUtils
import com.mxingo.passenger.util.DevInfo
import com.mxingo.passenger.util.TextUtil
import com.mxingo.passenger.widget.MyProgress
import com.mxingo.passenger.widget.ShowToast
import com.squareup.otto.Subscribe
import javax.inject.Inject


class LoginActivity : BaseActivity() {

    lateinit var llMobile: LinearLayout
    lateinit var llVcode: LinearLayout
    lateinit var etMobile: EditText
    lateinit var btnNext: Button
    lateinit var tvCountDown: TextView

    lateinit var tvVcodes: List<TextView>
    lateinit var tvMobile: TextView
    lateinit var etVcode: EditText


    var countDown: MyCountDownTimer = MyCountDownTimer()

    @Inject
    lateinit var presenter: MyPresenter
    private var userId = 0
    private lateinit var progress: MyProgress


    companion object {
        @JvmStatic
        fun startLoginActivity(activity: Activity) {
            activity.startActivityForResult(Intent(activity, LoginActivity::class.java), Constants.REQUEST_LOGIN_ACTIVITY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ComponentHolder.appComponent!!.inject(this)
        presenter.register(this)
        progress = MyProgress(this)

        llMobile = findViewById(R.id.ll_mobile) as LinearLayout
        llVcode = findViewById(R.id.ll_vcode) as LinearLayout
        etMobile = findViewById(R.id.et_mobile) as EditText
        btnNext = findViewById(R.id.btn_next) as Button
        tvCountDown = findViewById(R.id.tv_count_down) as TextView
        tvVcodes = arrayListOf(findViewById(R.id.tv_vcode_1) as TextView
                , findViewById(R.id.tv_vcode_2) as TextView
                , findViewById(R.id.tv_vcode_3) as TextView
                , findViewById(R.id.tv_vcode_4) as TextView)

        etVcode = findViewById(R.id.et_vcode) as EditText
        tvMobile = findViewById(R.id.tv_mobile) as TextView
        etMobile.requestFocus()
        etMobile.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editText: Editable?) {
                if (editText!!.length == 11) {
                    btnNext.setBackgroundColor(ContextCompat.getColor(this@LoginActivity, R.color.colorButtonBg))
                } else {
                    btnNext.setBackgroundColor(ContextCompat.getColor(this@LoginActivity, R.color.btn_next))
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        btnNext.setOnClickListener {
            if (TextUtil.isMobileNO(etMobile.text.toString())) {
                progress.show()
                presenter.getVcode(etMobile.text.toString())
            }
        }


        etVcode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                LogUtils.d("p0", p0.toString())
                initVcode(p0.toString())
                if (p0.toString().length == 4) {
                    loginOrReg()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        tvVcodes.forEach {
            it.setOnClickListener {
                etVcode.requestFocus()
                showInput()
            }
        }

        findViewById(R.id.img_mobile).setOnClickListener {
            finish()
        }
        findViewById(R.id.img_vcode_cancel).setOnClickListener {
            finish()
        }
        if(UserInfoPreferences.getInstance().userId != 0){
            etMobile.text.append(UserInfoPreferences.getInstance().mobile)
        }

        findViewById(R.id.tv_user_agreement).setOnClickListener {
            WebViewActivity.startWebViewActivity(this,"用户协议",Constants.USER_AGREEMENT)
        }
    }

    private fun loginOrReg() {
        progress.show()
        if (userId == 0) {
            presenter.reg(tvMobile.text.toString(), etVcode.text.toString(), 1, UserInfoPreferences.getInstance().devToken, DevInfo.getInfo())
        } else {
            presenter.login(tvMobile.text.toString(), etVcode.text.toString(), 1, UserInfoPreferences.getInstance().devToken, DevInfo.getInfo())
        }
    }

    private fun initVcode(text: String) {
        if (TextUtil.isEmpty(text)) {
            tvVcodes.forEach {
                it.isSelected = false
                it.text = ""
            }
            return
        } else {
            for (i in 0..text.length - 1) {
                tvVcodes[i].isSelected = true
                tvVcodes[i].text = text.split("")[i + 1]
            }
            for (i in text.length..tvVcodes.size - 1) {
                tvVcodes[i].isSelected = false
                tvVcodes[i].text = ""

            }
        }
    }

    private fun showInput() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }


    inner class MyCountDownTimer : CountDownTimer(60000, 1000) {
        override fun onFinish() {
            tvCountDown.text = "获取验证码"
        }

        override fun onTick(downTime: Long) {
            tvCountDown.text = "${downTime / 1000}秒后重发"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unregister(this)
    }

    @Subscribe
    fun loadData(any: Any) {
        when (any::class.java) {
            VCodeEntity::class.java -> {
                getVcode(any as VCodeEntity)
            }
            RegEntity::class.java -> {
                reg(any as RegEntity)
            }
            LoginEntity::class.java -> {
                login(any as LoginEntity)
            }
        }
    }

    private fun getVcode(vCode: VCodeEntity) {
        progress.dismiss()
        if (vCode.rspCode.equals("00")) {
            userId = vCode.usrId
            llMobile.visibility = View.GONE
            llVcode.visibility = View.VISIBLE
            tvMobile.text = etMobile.text
            countDown.start()
            etVcode.requestFocus()
            showInput()
        } else {
            ShowToast.showCenter(this, vCode.rspDesc)
        }
    }

    private fun reg(regEntity: RegEntity) {
        if (regEntity.rspCode.equals("00")) {
            UserInfoPreferences.getInstance().token = regEntity.rxToken
            presenter.login(tvMobile.text.toString(), etVcode.text.toString(), 1, UserInfoPreferences.getInstance().devToken, DevInfo.getInfo())
        } else {
            ShowToast.showCenter(this, regEntity.rspDesc)
            progress.dismiss()
        }
    }

    private fun login(loginEntity: LoginEntity) {
        progress.dismiss()
        if (loginEntity.rspCode.equals("00")) {
            UserInfoPreferences.getInstance().userId =loginEntity.usrId
            UserInfoPreferences.getInstance().token = loginEntity.rxToken
            UserInfoPreferences.getInstance().mobile = tvMobile.text.toString()
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            ShowToast.showCenter(this, loginEntity.rspDesc)
        }
    }
}
