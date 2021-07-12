package com.myfittinglife.equipmentinfoproject.simoperatorname

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.myfittinglife.equipmentinfoproject.R
import kotlinx.android.synthetic.main.activity_sim_operator_name.*
import kotlin.text.StringBuilder

/**
@Author LD
@Time 2021/6/25 16:13
@Describe 运营商名称获取
@Modify
 */

class SimOperatorNameActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ceshi"
    }

    var info = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sim_operator_name)

        btnSimOperatorName.setOnClickListener {

            getSimOperatorName()
        }
        btnNetOperatorName.setOnClickListener {
            getNetWorkOperatorName()
        }


        //获取MNC和MCC
        btnGetMNCMCC.setOnClickListener {
            getMNCAndMCC()
        }
        //清空内容
        btnClear.setOnClickListener {
            info.clear()
            tvInfo.text = info
        }
    }

    /**
     * 获取运营商名称
     * 不需要权限，各个版本均可获取
     * TODO:
     * 1、实际测试并未申请权限，为什么别人还需要
     * 2、实际卡测试来看看
     */
    private fun getSimOperatorName() {
        var opeType = "OTHER"
        val tm =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simOperator = tm.simOperator

        if (tm.simState != TelephonyManager.SIM_STATE_READY) {
            when (tm.simState) {
                TelephonyManager.SIM_STATE_ABSENT -> {//1
                    Log.i(TAG, "没有Sim卡")
                    info.append("没有Sim卡\n")
                }
                TelephonyManager.SIM_STATE_PIN_REQUIRED -> {//2
                    Log.i(TAG, "Sim卡状态锁定，需要PIN解锁")
                    info.append("Sim卡状态锁定，需要PIN解锁\n")
                }
                TelephonyManager.SIM_STATE_PUK_REQUIRED -> {//3
                    Log.i(TAG, "Sim卡状态锁定，需要PUK解锁")
                    info.append("Sim卡状态锁定，需要PUK解锁\n")
                }
                TelephonyManager.SIM_STATE_NETWORK_LOCKED -> {//4
                    Log.i(TAG, "需要网络PIN码解锁")
                    info.append("需要网络PIN码解锁\n")
                }
                //...
            }
            tvInfo.text = info
            return
        }
        Log.i(TAG, "getSimOperator()获取的MCC+MNC为：$simOperator")
        info.append("getSimOperator()获取的MCC+MNC为：$simOperator\n")
        Log.i(TAG, "getSimOperatorName()方法获取的运营商名称为:${tm.simOperatorName} ")
        info.append("getSimOperatorName()方法获取的运营商名称为:${tm.simOperatorName}\n ")


        opeType = if ("46001" == simOperator || "46006" == simOperator || "46009" == simOperator) {
            "中国联通"
        } else if ("46000" == simOperator || "46002" == simOperator || "46004" == simOperator || "46007" == simOperator) {
            "中国移动"
        } else if ("46003" == simOperator || "46005" == simOperator || "46011" == simOperator) {
            "中国电信"
        } else if ("46020" == simOperator) {
            "中国铁通"
        } else {
            "OHTER"
        }
        Log.i(TAG, "通过getSimOperator()人为判断的运营商名称是: $opeType")
        info.append("通过getSimOperator()人为判断的运营商名称是: $opeType\n")

        tvInfo.text = info
    }

    /**
     *
     * 获取运营商名称，
     * 当是CDMA网络时，通过networkOperator不可靠
     * 3G信号时才可使用该种方法来进行判断()，即getPhoneType()=TelephonyManager.PHONE_TYPE_CDMA才行
     */
    private fun getNetWorkOperatorName() {
        var opeType = "OTHER"
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        //用于判断拨号那张卡的运营商
        val networkOperator = tm.networkOperator
        Log.i(TAG, "getNetWorkOperator()获取的MCC+MNC为：$networkOperator")
        info.append("getNetWorkOperator()获取的MCC+MNC为：$networkOperator\n")
        if (tm.phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
            Log.i(TAG, "getNetWorkOperatorNameTest: 在CDMA网络上，不可靠，所以不用这种方法")
            info.append("getNetWorkOperatorNameTest: 在CDMA网络上，不可靠，所以不用这种方法\n")
            return
        }
        when (tm.phoneType) {
            0 -> {
                Log.i(TAG, "getPhoneType()网络类型为：NO_PHONE")
                info.append("getPhoneType()网络类型为：NO_PHONE\n")
            }
            1 -> {
                Log.i(TAG, "getPhoneType()网络类型为：GSM_PHONE")
                info.append("getPhoneType()网络类型为：GSM_PHONE\n")

            }
            2 -> {
                Log.i(TAG, "getPhoneType()网络类型为：CDMA_PHONE")
                info.append("getPhoneType()网络类型为：CDMA_PHONE\n")

            }
            3 -> {
                Log.i(TAG, "getPhoneType()网络类型为：SIP_PHONE")
                info.append("getPhoneType()网络类型为：SIP_PHONE\n")

            }
            4 -> {
                Log.i(TAG, "getPhoneType()网络类型为：THIRD_PARTY_PHONE")
                info.append("getPhoneType()网络类型为：THIRD_PARTY_PHONE\n")

            }
            5 -> {
                Log.i(TAG, "getPhoneType()网络类型为：IMS_PHONE")
                info.append("getPhoneType()网络类型为：IMS_PHONE\n")

            }
            6 -> {
                Log.i(TAG, "getPhoneType()网络类型为：CDMA_LTE_PHONE")
                info.append("getPhoneType()网络类型为：CDMA_LTE_PHONE\n")

            }
        }
        Log.i(TAG, "getNetworkOperatorName()方法获取的网络类型名称是: ${tm.networkOperatorName}")
        info.append("getNetworkOperatorName()方法获取的网络类型名称是: ${tm.networkOperatorName}\n")
        opeType =
            if ("46001" == networkOperator || "46006" == networkOperator || "46009" == networkOperator) {
                "中国联通"
            } else if ("46000" == networkOperator || "46002" == networkOperator || "46004" == networkOperator || "46007" == networkOperator) {
                "中国移动"
            } else if ("46003" == networkOperator || "46005" == networkOperator || "46011" == networkOperator) {
                "中国电信"
            } else {
                "OHTER"
            }
        Log.i(TAG, "通过getNetworkOperator()人为判断的运营商名称是: $opeType")
        info.append("通过getNetworkOperator()人为判断的运营商名称是: $opeType\n")
        tvInfo.text = info

    }

    /**
     * 需要READ_PHONE_STATE权限
     * 因为小米手机申请读取手机权限的时候，直接授予了空白的信息，所以为空，如果手动在设置里允许权限，就可以获取到了，而且获取到的是两个卡的信息，所以这里不建议使用这种方法
     * I/ceshi: mcc = 460 mnc = 02
     * I/ceshi: mcc = 460 mnc = 00
     */
    private fun getMNCAndMCC() {
        //5.1及以上版本才可使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val permissionList = mutableListOf<String>()
            permissionList.add(android.Manifest.permission.READ_PHONE_STATE)
            if (requestPermission(1, permissionList)) {
                Log.i(TAG, "getMNCAndMCC: 已获取READ_PHONE_STATE权限")
                val subscriptionManager: SubscriptionManager =
                    getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
                //22需要版本5.1及以上才行
                val list = mutableListOf<SubscriptionInfo>()
                //上面的requestPermission()方法已经进行了判断
                if (subscriptionManager.activeSubscriptionInfoList != null) {
                    list.addAll(subscriptionManager.activeSubscriptionInfoList)
                    if (list.size > 0) {
                        for (s in list) {
                            //10.0以上用下面的方法获取，10.0以下用上面的方法获取
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                Log.i(
                                    TAG,
                                    "卡槽${s.simSlotIndex}：mcc = " + s.mccString + " mnc = " + s.mncString
                                )
                                info.append("卡槽${s.simSlotIndex}：mcc = " + s.mccString + " mnc = " + s.mncString + "\n")
                            } else {
                                Log.i(TAG, "卡槽${s.simSlotIndex}：mcc = " + s.mcc + " mnc = " + s.mnc)
                                info.append("卡槽${s.simSlotIndex}：mcc = " + s.mcc + " mnc = " + s.mnc + "\n")
                            }
                        }
                    } else {
                        Log.i(TAG, "getMNCAndMCC: 获取到的信息为空")
                        info.append("getMNCAndMCC: 获取到的信息为空\n")
                    }
                } else {
                    Log.i(TAG, "getMNCAndMCC: 获取到的信息为空")
                    info.append("getMNCAndMCC: 获取到的信息为空\n")
                }
            }
            tvInfo.text = info

        }
    }

    //--------
    /**
     * 申请权限封装，可传多个权限
     */
    private fun requestPermission(requestCode: Int, permissionList: List<String>): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //没有同意需要申请的权限
            val requestPermissionList = mutableListOf<String>()
            for (permission in permissionList) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionList.add(permission)
                }
            }
            if (requestPermissionList.size > 0) {
                ActivityCompat.requestPermissions(
                    this,
                    requestPermissionList.toTypedArray(),
                    requestCode
                )
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            //READ_PHONE_STATE权限申请
            1 -> {
                if (verifyResult(grantResults)) {
                    getMNCAndMCC()
                }
            }
        }
    }

    /**
     *验证权限申请的结果
     */
    private fun verifyResult(grantResults: IntArray): Boolean {
        if (grantResults.isNotEmpty()) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "必须同意所有权限才能使用该功能", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            return true
//            getIMEI()
        } else {
            Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}