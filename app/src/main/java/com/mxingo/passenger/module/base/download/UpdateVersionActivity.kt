package com.mxingo.passenger.module.base.download

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.Button
import android.widget.TextView
import butterknife.ButterKnife
import com.mxingo.driver.module.BaseActivity
import com.mxingo.driver.utils.Constants
import com.mxingo.passenger.R

class UpdateVersionActivity : BaseActivity() {

    private lateinit var tvUpdateContent: TextView
    private lateinit var btnUpdateIdOk: Button
    private lateinit var btnUpdateIdCancel: Button
    private var versionEntity: VersionEntity? = null

    companion object {
        @JvmStatic
        fun startUpdateVersionActivity(context: Context, versionEntity: VersionEntity) {
            context.startActivity(Intent(context, UpdateVersionActivity::class.java).putExtra(Constants.ACTIVITY_DATA, versionEntity))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_version)
        ButterKnife.bind(this)

        tvUpdateContent = findViewById(R.id.tv_update_content) as TextView
        btnUpdateIdOk = findViewById(R.id.btn_update_id_ok) as Button
        btnUpdateIdCancel = findViewById(R.id.btn_update_id_cancel) as Button

        versionEntity = intent.getSerializableExtra(Constants.ACTIVITY_DATA) as VersionEntity
        tvUpdateContent.text = "版本:${versionEntity!!.version}\n大小: ${versionEntity!!.size}\n\n${versionEntity!!.log}"
        if(versionEntity == null){
            finish()
        }
        btnUpdateIdOk.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.permissionMain)
            } else {
                DownloadApp(versionEntity).startDownload()
            }
            if (!versionEntity!!.isMustUpdate) {
                finish()
            }
        }

        if (versionEntity!!.isMustUpdate) {
            btnUpdateIdCancel.visibility = View.GONE
        }
        btnUpdateIdCancel.setOnClickListener {
            if (!versionEntity!!.isMustUpdate) {
                finish()
            }
        }

    }

    override fun onBackPressed() {
        if (!versionEntity!!.isMustUpdate) {
            super.onBackPressed()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.permissionMain && grantResults[0] == 0){
            DownloadApp(versionEntity).startDownload()
        }
    }
}
