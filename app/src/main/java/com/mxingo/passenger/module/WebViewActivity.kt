package com.mxingo.passenger.module

import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.module.base.log.LogUtils

class WebViewActivity : BaseActivity() {

    private lateinit var wv: WebView
    private lateinit var url: String
    private lateinit var title: String

    companion object {
        @JvmStatic
        fun startWebViewActivity(context: Context, title: String, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(Constants.TITLE, title)
            intent.putExtra(Constants.URL, url)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        wv = findViewById(R.id.wv) as WebView

        url = intent.getStringExtra(Constants.URL)

        title = intent.getStringExtra(Constants.TITLE)
        LogUtils.d("TAG", title + "," + url)

        setToolbar(toolbar = findViewById(R.id.toolbar) as Toolbar)
        (findViewById(R.id.tv_toolbar_title) as TextView).text = title

        wv.loadUrl(url)
        val webSetting = wv.settings
        webSetting.javaScriptEnabled = true
        webSetting.setSupportZoom(false)
        webSetting.domStorageEnabled = true

        wv.webChromeClient = MyWebChromeClient()
        wv.webViewClient = MyWebViewClient()
    }

    inner class MyWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            if (view.url.contains("https")) {
                handler.proceed()
            }
        }

//        @RequiresApi(Build.VERSION_CODES.M)
//        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
//            super.onReceivedError(view, request, error)
//        }
    }
}