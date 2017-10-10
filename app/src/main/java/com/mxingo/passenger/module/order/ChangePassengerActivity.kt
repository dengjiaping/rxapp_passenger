
@file:Suppress("DEPRECATION")
package com.mxingo.passenger.module.order

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R
import com.mxingo.passenger.module.base.log.LogUtils
import com.mxingo.passenger.util.TextUtil
import com.mxingo.passenger.widget.ShowToast


class ChangePassengerActivity : AppCompatActivity() {
    private lateinit var etPassenger: EditText
    private lateinit var etPassengerMobile: EditText

    companion object {
        @JvmStatic
        fun startChangePassengerActivity(activity: Activity) {
            activity.startActivityForResult(Intent(activity, ChangePassengerActivity::class.java), Constants.REQUEST_ChANGE_PASSENGER_ACTIVITY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_passenger)
        etPassenger = findViewById(R.id.et_passenger) as EditText
        etPassengerMobile = findViewById(R.id.et_passenger_mobile) as EditText
        findViewById(R.id.btn_cancel).setOnClickListener { finish() }
        findViewById(R.id.btn_ok).setOnClickListener {
            if (TextUtil.isEmpty(etPassenger.text.toString())) {
                ShowToast.showCenter(this, "请输入乘客姓名")
                return@setOnClickListener
            }

            if (TextUtil.isEmpty(etPassengerMobile.text.toString())) {
                ShowToast.showCenter(this, "请输入乘客手机号")
                return@setOnClickListener
            }

            if (!TextUtil.isMobileNO(etPassengerMobile.text.toString())) {
                ShowToast.showCenter(this, "乘客手机号不正确")
                return@setOnClickListener
            }

            setResult(Activity.RESULT_OK, Intent()
                    .putExtra(Constants.ACTIVITY_PASSENGER_NAME, etPassenger.text.toString())
                    .putExtra(Constants.ACTIVITY_PASSENGER_MOBILE, etPassengerMobile.text.toString()))
            finish()
        }
        findViewById(R.id.tv_directory).setOnClickListener {
            addPermissions()
        }
    }

    fun addPermissions() {

        val list = arrayListOf(Manifest.permission.READ_CONTACTS)
        list.filter {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }.map {
            list.remove(it)
        }

        if (list.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, list.toArray(Array<String>(list.size, { i -> i.toString() })), Constants.permissionMain)
        } else {
//            startActivityForResult(Intent(Intent.ACTION_PICK,
//                    ContactsContract.Contacts.CONTENT_URI), Constants.REQUEST_DIRECTORY_ACTIVITY)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(intent, Constants.REQUEST_DIRECTORY_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // ContentProvider展示数据类似一个单个数据库表
            // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据

            // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            val contactData = data!!.data
            // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
            val cursor = managedQuery(contactData, null, null, null, null) ?: return
            cursor.moveToFirst()
            // 获得DATA表中的名字
            etPassenger.text.clear()
            etPassenger.text.append(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
            val mobile =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            LogUtils.d("mobile",""+mobile)
            if (!TextUtil.isEmpty(mobile) && TextUtil.isMobileNO(mobile.replace(" ","").replace("-",""))) {
                etPassengerMobile.text.clear()
                etPassengerMobile.text.append(mobile.replace(" ","").replace("-",""))
            }
            cursor.close()
                    // 条件为联系人ID
//            val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
  //          // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
//            val reContentResolverol = contentResolver
//            val phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null)
//            val phone = reContentResolverol.query( Uri.parse(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE), null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null)
//
//            while (phone!!.moveToNext()) {
//                val mobile = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "")
//                if (!TextUtil.isEmpty(mobile) && TextUtil.isMobileNO(mobile)) {
//                    etPassengerMobile.text.clear()
//                    etPassengerMobile.text.append(mobile)
//                    break
//                }
//
//            }

        }
    }
}
