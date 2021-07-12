package com.myfittinglife.equipmentinfoproject.mac

import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.myfittinglife.equipmentinfoproject.R
import kotlinx.android.synthetic.main.activity_mac.*

/**
 @Author LD
 @Time 2021/6/30 16:13
 @Describe 获取Mac地址
 @Modify
*/
class MacActivity : AppCompatActivity() {

    companion object{
        const val TAG = "ceshi"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mac)
        btnMac.setOnClickListener {
            var address = getConnectedWifiMacAddress(this)
            Log.i(TAG, "onCreate: 地址为：$address")
        }
    }

    /**
     * 获取Wifi的mac地址(这个是连接的wifi的mac地址而不是手机wifi模块的mac地址)
     */
    private fun getConnectedWifiMacAddress(context: Context): String? {
        var connectedWifiMacAddress: String? = null
        val wifiManager = context.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiList: List<ScanResult>?
        if (wifiManager != null) {
            wifiList = wifiManager.scanResults
            val info = wifiManager.connectionInfo
            if (wifiList != null && info != null) {
                for (element in wifiList) {
                    val result: ScanResult = element
                    if (info.bssid == result.BSSID) {
                        connectedWifiMacAddress = result.BSSID
                        Log.i(TAG, "getConnectedWifiMacAddress: BSSID为：${result.BSSID}")
                        Log.i(TAG, "getConnectedWifiMacAddress: SSID为：${result.SSID}")
                    }
                }
            }
        }
        return connectedWifiMacAddress
    }
}