package com.mxingo.driver.module


import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.mxingo.passenger.R
import com.mxingo.passenger.module.StartPageActivity
import com.mxingo.passenger.util.HttpUtil
import com.umeng.analytics.MobclickAgent


@Suppress("DEPRECATED_IDENTITY_EQUALS")
open class BaseActivity : AppCompatActivity() {


    fun setToolbar(toolbar: Toolbar) {
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        HttpUtil.checkNetwork(this)
        if (this::class.java.equals(StartPageActivity::class.java)) {
            MobclickAgent.onPageStart(this.packageName)
        }
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        if (this::class.java.equals(StartPageActivity::class.java)) {
            MobclickAgent.onPageEnd(this.packageName)
        }
        MobclickAgent.onPause(this)
    }
}
