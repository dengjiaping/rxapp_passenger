package com.mxingo.passenger.module

import android.os.Bundle
import android.os.Handler
import com.mxingo.driver.module.BaseActivity
import com.mxingo.passenger.MyApplication
import com.mxingo.passenger.R

class StartPageActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)

        MyApplication.bus.post(Any())

        Handler().postDelayed({
            MainActivity.startMainActivity(this)
            finish()
        }, 4000)
    }
}
