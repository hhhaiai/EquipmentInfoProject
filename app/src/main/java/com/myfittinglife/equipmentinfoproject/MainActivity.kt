package com.myfittinglife.equipmentinfoproject

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.myfittinglife.equipmentinfoproject.imei.IMEIActivity
import com.myfittinglife.equipmentinfoproject.location.LocationActivity
import com.myfittinglife.equipmentinfoproject.mac.MacActivity
import com.myfittinglife.equipmentinfoproject.oaid.OAIDActivity
import com.myfittinglife.equipmentinfoproject.simoperatorname.SimOperatorNameActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

/**
@Author LD
@Time 2021/6/24 10:46
@Describe 设备信息获取汇总
@Modify
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val TAG = "ceshi"

        //IMEI需要的权限申请
        const val IMEI_REQUEST_CODE = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnSimOperatorName.setOnClickListener(this)
        btnAndroidID.setOnClickListener(this)
        btnIMEI.setOnClickListener(this)
        btnOAID.setOnClickListener(this)
        btnImsi.setOnClickListener(this)
        btnIP.setOnClickListener(this)
        btnMac.setOnClickListener(this)
        btnLocation.setOnClickListener(this)
        btnClear.setOnClickListener {
            tvInfo.text=""
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //获取运营商名称
            R.id.btnSimOperatorName -> {
                startActivity(Intent(this,SimOperatorNameActivity::class.java))
            }
            //获取AndroidID
            R.id.btnAndroidID -> {
                getAndroidID()
            }
            //获取IMEI
            R.id.btnIMEI -> {
                startActivity(Intent(this,IMEIActivity::class.java))
            }
            //获取OAID
            R.id.btnOAID -> {
                startActivity(Intent(this, OAIDActivity::class.java))
            }
            //获取IMSI
            R.id.btnImsi->{
                getIMSI()
            }
            //获取设备IP
            R.id.btnIP->{

            }
            //获取Mac地址
            R.id.btnMac->{
                startActivity(Intent(this,MacActivity::class.java))
            }
            //获取地理位置信息
            R.id.btnLocation->{
                startActivity(Intent(this,LocationActivity::class.java))
            }

        }
    }


    /**
     * 获取AndroidID
     * 不需要权限，各个版本均可获取
     */
    private fun getAndroidID() {

        val androidID = Settings.System.getString(
            contentResolver, Settings.Secure.ANDROID_ID
        )
        Log.i(TAG, "AndroidID为:$androidID")
        tvInfo.text = "AndroidID为:$androidID"
    }

    /**
     * 删除，这个得到的不是IMSI
     */
    fun getIMSI(){
        val telManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            var imsi = telManager.subscriptionId
            Log.i(TAG, "getIMSI: $imsi")
        }else{
            Log.i(TAG, "getIMSI: 版本太低获取不到")
        }

    }

}