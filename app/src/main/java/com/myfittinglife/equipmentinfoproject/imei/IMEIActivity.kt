package com.myfittinglife.equipmentinfoproject.imei

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import com.myfittinglife.equipmentinfoproject.MainActivity
import com.myfittinglife.equipmentinfoproject.R
import com.myfittinglife.equipmentinfoproject.utils.PermissionUtil
import kotlinx.android.synthetic.main.activity_i_m_e_i.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.tvInfo
import java.lang.StringBuilder

/**
 @Author LD
 @Time 2021/7/2 9:10
 @Describe 获取IMEI
 @Modify
*/

class IMEIActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ceshi"

        //IMEI需要的权限申请
        const val IMEI_REQUEST_CODE = 1
    }


    private val imeiStringBuilder= StringBuilder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_i_m_e_i)

        btnGetIMEIInfo.setOnClickListener {
            getIMEI()
        }
    }



    /**
     * 获取IMEI
     * 需要动态申请READ_PHONE_STATE权限
     * Android10及以上版本获取不到
     */
    private fun getIMEI() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            val permissionList = mutableListOf<String>()
            permissionList.add(android.Manifest.permission.READ_PHONE_STATE)
            //判断是否有READ_PHONE_STATE权限
            if (PermissionUtil.requestPermission(IMEI_REQUEST_CODE, permissionList,this)){
                val telManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

//                var subscribeId = telManager.subscriberId
//                Log.i(TAG, "subscribeId为: $subscribeId")

                val IMEI = telManager.deviceId
                Log.i(TAG, "deviceId为：${IMEI}")
                imeiStringBuilder.append("deviceId为：${IMEI}\n")
                val index1 = telManager.getImei(0)
                val index2 = telManager.getImei(1)
                Log.i(TAG, "IMEI卡槽1：$index1")
                imeiStringBuilder.append("IMEI卡槽1：$index1 \n")
                Log.i(TAG, "IMEI卡槽2：$index2")
                imeiStringBuilder.append("IMEI卡槽2：$index2 \n")

                val meid1 = telManager.getMeid(0)
                val meid2 = telManager.getMeid(1)
                Log.i(TAG, "MEID卡槽1为: $meid1")
                imeiStringBuilder.append("MEID卡槽1为: $meid1 \n")
                Log.i(TAG, "MEID卡槽2为:$meid2 ")
                imeiStringBuilder.append("MEID卡槽2为:$meid2 \n")
            }
        }else{
            Log.i(TAG, "Android10及以上版本禁止获取IMEI")
            imeiStringBuilder.append("Android10及以上版本禁止获取IMEI \n")
        }
        imeiStringBuilder.append("---------\n")
        tvIMEIInfo.text = imeiStringBuilder

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            //READ_PHONE_STATE权限申请
            IMEI_REQUEST_CODE -> {
                if (PermissionUtil.verifyResult(grantResults,this)) {
                    getIMEI()
                }
            }
        }
    }
}