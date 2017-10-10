package com.mxingo.passenger.module.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.module.base.http.MyPresenter
import com.mxingo.passenger.widget.MyProgress
import javax.inject.Inject

class MessageActivity : BaseActivity() {

    @Inject
    lateinit var presenter: MyPresenter
    private lateinit var progress: MyProgress
    private var userId = 0

    companion object {
        @JvmStatic
        fun startMessageActivity(context: Context,userId:Int) {
            context.startActivity(Intent(context, MessageActivity::class.java).putExtra(Constants.USER_ID,userId))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setToolbar(toolbar)
        val tvToolbar = findViewById(R.id.tv_toolbar_title) as TextView
        tvToolbar.text = "消息中心"
    }
}
