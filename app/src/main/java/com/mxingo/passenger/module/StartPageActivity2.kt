//package com.mxingo.passenger.module
//
//import android.content.Context
//import android.os.Bundle
//import android.os.Handler
//import android.telephony.TelephonyManager
//import com.mxingo.driver.module.BaseActivity
//import com.mxingo.passenger.MyApplication
//import com.mxingo.passenger.R
//import com.mxingo.passenger.module.base.log.LogUtils
//import java.io.File
//
//class StartPageActivity2 : BaseActivity() {
//    private val kSystemRootStateUnknow = -1
//    private val kSystemRootStateDisable = 0
//    private val kSystemRootStateEnable = 1
//    private var systemRootState = kSystemRootStateUnknow
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_start_page)
//        MyApplication.bus.post(Any())
//        //执行下一步限制：非模拟器，非root
////        if (isEmulator() && !isRoot()) {
////            Handler().postDelayed({
////                MainActivity.startMainActivity(this)
////                finish()
////            }, 2000)
////        } else {
////            ShowToast.showCenter(this, "请在没有ROOT手机上运行")
////            return
////        }
//        Handler().postDelayed({
//            MainActivity.startMainActivity(this)
//            finish()
//        }, 4000)
//    }
//
//    fun isEmulator(): Boolean {
//        //获取手机的运营商
//        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信
//        val context = window.context
//        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        val IMSI = telephonyManager.subscriberId
//        LogUtils.d("-----IMSI", IMSI)
//        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
//        return if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46001") || IMSI.startsWith("46003")) {
//            true//真机
//        } else false//模拟器
//    }
//
//    // 判断机器Android是否已经root，即是否获取root权限
//    fun isRoot(): Boolean {
//        if (systemRootState == kSystemRootStateEnable) {
//            return true
//        } else if (systemRootState == kSystemRootStateDisable) {
//            return false
//        }
//        var file: File?
//        val kSuSearchPaths = arrayOf("/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/")
//        try {
//            for (i in kSuSearchPaths.indices) {
//                file = File(kSuSearchPaths[i] + "su")
//                if (file.exists()) {
//                    systemRootState = kSystemRootStateEnable
//                    return true
//                }
//            }
//        } catch (e: Exception) {
//        }
//        systemRootState = kSystemRootStateDisable
//        return false
//    }
//}