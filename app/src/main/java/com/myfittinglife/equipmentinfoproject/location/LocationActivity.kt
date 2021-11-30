package com.myfittinglife.equipmentinfoproject.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.myfittinglife.equipmentinfoproject.R
import com.myfittinglife.equipmentinfoproject.utils.PermissionUtil
import kotlinx.android.synthetic.main.activity_location.*
import java.lang.StringBuilder


/**
@Author LD
@Time 2021/7/2 9:23
@Describe 获取地理位置信息
@Modify
 */
class LocationActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ceshi"
        const val LOCATION_REQUEST_CODE = 1
        const val LISTENER_REQUEST_CODE = 2
    }

    private val permissionList =
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )


    private var locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.i(TAG, "onLocationChanged: 经纬度发生变化")
            //调用更新位置
            updateToNewLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            updateToNewLocation(null)
            Log.i(TAG, "onProviderDisabled: ")
        }

        override fun onProviderEnabled(provider: String) {
            Log.i(TAG, "onProviderEnabled: ")
        }
    }

    //获取位置服务
    private lateinit var locationManager: LocationManager

    //用来在屏幕上显示位置信息
    private val locationInfo = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //获取位置信息
        btnLocationInfo.setOnClickListener {

            //查看支持获取经纬度的方式有哪些
            var supportWay = StringBuilder()
            //passive：被动的从其他程序获取
            locationManager.getProviders(true).let {
                if (it.size > 0) {
                    for (provider in it) {
                        supportWay.append(provider + "\t")
                    }
                    locationInfo.append("支持的获取经纬度的方式有:$supportWay").append("\n")
                }
            }
            Log.i(TAG, "支持的获取经纬度的方式有: $supportWay")

            //（一、申请权限）
            if (PermissionUtil.requestPermission(LOCATION_REQUEST_CODE, permissionList.toList(), this)) {
                //获取经纬度信息
                getLocationInfo()
            }


        }
        //设置监听器，周期性获取定位信息
        btnGetCycleLocation.setOnClickListener {
            locationMonitor()
        }
        //跳转到系统设置处来打开权限
        btnOpenSetting.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(intent, 887)
        }
        //判断谷歌服务是否支持
        btnGoogleSupport.setOnClickListener {
           isSupportGoogleService()
        }
    }


    /**
     * 查看手机是否支持谷歌服务()
     */
    private fun isSupportGoogleService() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        var resultCode = apiAvailability.isGooglePlayServicesAvailable(this)

        //关闭谷歌基础服务返回SERVICE_DISABLED，开启谷歌基础服务返回SUCCESS
        Log.i(TAG, "isSupportGoogleService: $resultCode")

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Log.i(TAG, "isSupportGoogleService: 人为可以解决的错误")
                GoogleApiAvailability.getInstance().getErrorDialog(this, resultCode, 200).show();
            }
        }

    }

    /**
     * 判断定位服务是否开启
     */
    private fun isLocationServiceOpen(): Boolean {
        var gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        var network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        //有一个开启就可
        return gps || network
    }

    /**
     * 获取地理位置信息
     */
    @SuppressLint("MissingPermission")
    fun getLocationInfo() {

        //判断是否开启位置服务（二）
        if (isLocationServiceOpen()) {
            //获取所有支持的provider（三）
            var providers = locationManager.getProviders(true)
            //存储最优的结果
            var betterLocation: Location? = null
            for (provider in providers) {
                val location = locationManager.getLastKnownLocation(provider)
                location?.let {
                    Log.i(TAG, "$provider 精度为：${it.accuracy}")
                    locationInfo.append("$provider 精度为：${it.accuracy} \n")
                    if (betterLocation == null) {
                        betterLocation = it
                    } else {
                        //因为半径等于精度，所以精度越低代表越准确
                        if (it.accuracy < betterLocation!!.accuracy)
                            betterLocation = it
                    }
                }
                if (location == null) {
                    Log.i(TAG, "$provider 获取到的位置为null")
                    locationInfo.append("$provider 获取到的位置为null \n")
                }
            }
            betterLocation?.let {
                Log.i(TAG, "精度最高的获取方式：${it.provider} 经度：${it.longitude}  纬度：${it.latitude}")
                locationInfo.append("精度最高的获取方式：${it.provider} 经度：${it.longitude}  纬度：${it.latitude} \n")
            }
            //（四）若所支持的定位获取方式获取到的都是空，则开启连续定位服务
            if (betterLocation == null) {
                for (provider in locationManager.getProviders(true)) {
                    locationMonitor(provider)
                }
                Log.i(TAG, "getLocationInfo: 获取到的经纬度均为空，已开启连续定位监听")
                locationInfo.append("getLocationInfo: 获取到的经纬度均为空，已开启连续定位监听 \n")
            }
            tvLocationInfo.text = locationInfo
        } else {
            Toast.makeText(this, "请跳转到系统设置中打开定位服务", Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * 连续位置监听
     * 获取连续的点位信息
     * 定位模式、更新的时间单位(毫秒)、更新的距离单位(米)、位置信息的监听
     */
    @SuppressLint("MissingPermission")
    fun locationMonitor() {
        if (PermissionUtil.requestPermission(
                LISTENER_REQUEST_CODE,
                permissionList.toList(),
                this
            )
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                3000.toLong(),
                0.toFloat(),
                locationListener
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                3000.toLong(),
                0.toFloat(),
                locationListener
            )
        }
    }

    @SuppressLint("MissingPermission")
    fun locationMonitor(provider: String) {
        if (PermissionUtil.requestPermission(
                LISTENER_REQUEST_CODE,
                permissionList.toList(),
                this
            )
        ) {
            Log.i(TAG, "locationMonitor: 开启了连续定位$provider")
            locationManager.requestLocationUpdates(
                provider,
                3000.toLong(),
                0.toFloat(),
                locationListener
            )
        }
    }

    /**
     * 更新位置信息
     */
    fun updateToNewLocation(location: Location?) {
        //纬度
        var lat = ""
        //经度
        var lng = ""
        if (location != null) {
            lat = location.latitude.toString()
            lng = location.longitude.toString()
            Log.i(TAG, "updateToNewLocation: 经度为：$lng")
            Log.i(TAG, "updateToNewLocation: 纬度为：$lat")
            locationInfo.append("updateToNewLocation经度为：$lng  纬度为：$lat\n")
            tvLocationInfo.text = locationInfo
        } else {
            Log.i(TAG, "updateToNewLocation: location为空")
        }
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener)
        } else {
            Toast.makeText(this, "无法获取地理信息，请确认已开启定位权限并选择定位模式为GPS、WLAN和移动网络", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * 权限申请回调
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            //LOCATION_REQUEST_CODE权限申请
            LOCATION_REQUEST_CODE -> {
                if (PermissionUtil.verifyResult(grantResults, this)) {
                    getLocationInfo()
                } else {
                    Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show()
                }
            }
            LISTENER_REQUEST_CODE -> {
                if (PermissionUtil.verifyResult(grantResults, this)) {
                    locationMonitor()
                } else {
                    Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}