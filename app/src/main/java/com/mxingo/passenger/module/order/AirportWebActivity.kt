package com.mxingo.passenger.module.order

import android.app.Activity
import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.webkit.*
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener
import com.baidu.mapapi.search.poi.PoiDetailResult
import com.baidu.mapapi.search.poi.PoiIndoorResult
import com.baidu.mapapi.search.poi.PoiResult
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.module.base.log.LogUtils
import com.mxingo.passenger.module.base.map.BaiduMapUtil


class AirportWebActivity : BaseActivity() {

    private lateinit var wvAirport: WebView

    companion object {
        @JvmStatic
        fun startAirportWebActivity(context: Activity, url: String) {
            context.startActivityForResult(Intent(context, AirportWebActivity::class.java)
                    .putExtra(Constants.URL, url), Constants.REQUEST_AIRPORT_ACTIVITY)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airport_web)

        wvAirport = findViewById(R.id.wv_airport) as WebView
        initWeb()
    }

    fun initWeb() {
        wvAirport.loadUrl(intent.getStringExtra(Constants.URL))
        wvAirport.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
        wvAirport.settings.javaScriptEnabled = true //设置支持Javascript
        wvAirport.settings.setSupportZoom(false)
        wvAirport.settings.domStorageEnabled = true
        wvAirport.requestFocus()
        wvAirport.addJavascriptInterface(this, "airport")

        wvAirport.webChromeClient = MyWebChromeClient()
        wvAirport.webViewClient = MyWebViewClient()
    }

    inner class MyWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            if (view.url.contains("https://")) {
                handler.proceed()
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
        }
    }

    @JavascriptInterface
    fun getAirport(type: Int, city: String, airport: String, threeCode: String) {
        LogUtils.d("data", "$type,$city,$airport,$threeCode")
        if (type == 1 || type == 2) {
            getAirport(type, city)
        } else if (type == 3) {
            BaiduMapUtil.getInstance().getPoiByPoiSearch(city,airport, 0, object : OnGetPoiSearchResultListener {
                override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {
                }

                override fun onGetPoiResult(p0: PoiResult?) {
                    if (p0 != null && p0.allPoi != null && !p0.allPoi.isEmpty()) {
                        LogUtils.d("data2", airport + "," + p0.allPoi[0].address + "," + p0.allPoi[0].city)
                        setResult(Activity.RESULT_OK, Intent()
                                .putExtra(Constants.ACTIVITY_BACK_AIRPORT, airport)
                                .putExtra(Constants.ACTIVITY_BACK_AIRPORT_THREE_CODE, threeCode)
                                .putExtra(Constants.ACTIVITY_BACK_DATA, p0.allPoi[0]))
                        finish()
                    } else {
                        finish()
                    }
                }

                override fun onGetPoiDetailResult(p0: PoiDetailResult?) {
                }

            })
        }
    }

    @JavascriptInterface
    fun getAirport(type: Int, city: String) {
        LogUtils.d("data", "" + type + "," + city)
        if (type == 1) {
            finish()
        } else if (type == 2) {
            setResult(Activity.RESULT_OK, Intent().putExtra(Constants.ACTIVITY_BACK_AIRPORT, city))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BaiduMapUtil.getInstance().poiSearchDestroy()
    }
}
