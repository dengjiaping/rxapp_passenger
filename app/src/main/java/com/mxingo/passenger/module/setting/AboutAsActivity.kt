package com.mxingo.passenger.module.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.mxingo.driver.module.BaseActivity
import com.mxingo.passenger.R

class AboutAsActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun startAboutAsActivity(context: Context) {
            context.startActivity(Intent(context, AboutAsActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_as)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setToolbar(toolbar)
        val tvToolbar = findViewById(R.id.tv_toolbar_title) as TextView
        tvToolbar.text = "关于我们"
    }
}
