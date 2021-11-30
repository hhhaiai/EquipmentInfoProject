package com.myfittinglife.equipmentinfoproject.ip

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.myfittinglife.equipmentinfoproject.R
import kotlinx.android.synthetic.main.activity_i_p.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

/**
@Author LD
@Time 2021/11/25 10:45
@Describe IP地址获取
@Modify
 */
class IPActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        const val TAG ="ceshi_IPActivity"
    }

    private val ipStringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_i_p)

        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btnNetType.setOnClickListener(this)
        btnNetIP.setOnClickListener {
            getNetAddress()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn1 -> {
                //移动流量下IP获取、Wifi下内网IP获取，这个方法通用
                val ip = getMobileIP()
                ipStringBuilder.append("移动流量下IP为:$ip\n")
                tvIPInfo.text = ipStringBuilder
            }
            R.id.btn2 -> {
                //Wifi下内网IP获取
                val ip = intIP2StringIP(getWifiIP())
                ipStringBuilder.append("Wifi下内网IP为:$ip\n")

                tvIPInfo.text = ipStringBuilder
            }
            R.id.btnNetType -> {
                when (judgeNetType()) {
                    0 -> {
                        ipStringBuilder.append("未知网络\n")
                        tvIPInfo.text = ipStringBuilder
                    }
                    1 -> {
                        ipStringBuilder.append("移动网络\n")
                        tvIPInfo.text = ipStringBuilder
                    }
                    2 -> {
                        ipStringBuilder.append("WIFI网络\n")
                        tvIPInfo.text = ipStringBuilder
                    }
                    3 -> {
                        ipStringBuilder.append("没有网络\n")
                        tvIPInfo.text = ipStringBuilder
                    }
                }
            }
        }
    }

    /**
     *
     * 获取移动网络下的外网IP、或wifi下的内网IP
     */
    private fun getMobileIP(): String? {
        //移动数据
        try {
            //返回本机的所有接口，枚举类型;
            //至少包含一个元素，代表回路isLoopbackAddress;
            //只支持该机器内实体间能进行通信的接口
            //getNetworkInterfaces()+getInetAddresses()可以获取该节点的所有IP地址
            val networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces()
            //枚举进行遍历
            while (networkInterfaceEnumeration.hasMoreElements()) {
                val networkInterface = networkInterfaceEnumeration.nextElement()

                val inetAddressEnumeration = networkInterface.inetAddresses
                //枚举进行遍历
                while (inetAddressEnumeration.hasMoreElements()) {
                    val inetAddress = inetAddressEnumeration.nextElement()
                    Log.i(TAG, "getMobileIP: IP地址-------：${inetAddress.hostAddress}")
//                    当不是回路地址且是IPV4时
//                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
//                        return inetAddress.getHostAddress()
//                    }
                    if (!inetAddress.isLoopbackAddress) {
                        if(inetAddress is  Inet4Address){
                            Log.i(TAG, "getMobileIP: 是Inet4Address${inetAddress.getHostAddress()}")
//                            return inetAddress.getHostAddress()
                        }
//                        else if (inetAddress is Inet6Address) {
//                            Log.i(TAG, "getMobileIP: 是Inet6Address${inetAddress.getHostAddress()}")
//                            return inetAddress.getHostAddress()
//                        }
                    }
                }
            }
        } catch (e: SocketException) {
            return null
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取Wifi下的IP，内网IP
     * 需要权限ACCESS_WIFI_STATE
     */
    private fun getWifiIP(): Int {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        Log.i(TAG, "getWifiIP: ipAddress为：${wifiInfo.ipAddress}")
        return wifiInfo.ipAddress
    }


    /**
     * 将得到的int类型的IP转换为String类型
     * @param ip
     * @return
     */
    private fun intIP2StringIP(ip: Int): String? {
        return  (ip and 0xFF).toString() + "." +
                (ip shr 8  and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }

    /**
     * 判断是移动网络还是Wifi
     * 0:未知网络
     * 1:移动网络
     * 2：Wifi
     * 3:没有网络
     */
    private fun judgeNetType():Int{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            //安卓6以下
            val mNetworkInfo = connectivityManager.activeNetworkInfo
            return if (mNetworkInfo != null) {
                when (mNetworkInfo.type) {
                    ConnectivityManager.TYPE_MOBILE -> {
                        1
                    }
                    ConnectivityManager.TYPE_WIFI -> {
                        2
                    }
                    else -> {
                        0
                    }
                }
            }else{
                3
            }
        }else{
            val network = connectivityManager.activeNetwork
            if (network != null) {
                val nc = connectivityManager.getNetworkCapabilities(network)
                return if (nc != null){
                    when {
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            1
                        }
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            2
                        }
                        else -> {
                            0
                        }
                    }
                }else{
                    3
                }
            }else{
                return 3
            }
        }
    }


    /**
     * 通过网络请求来获取IP地址
     */
    private fun getNetAddress(){

        val retrofit= Retrofit.Builder()
            .baseUrl("http://a")
            .build()
        val api =retrofit.create(IPService::class.java)
        api.getIP("http://pv.sohu.com/cityjson").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                response.body()?.string()?.let {
                    val start: Int = it.indexOf("{")
                    val end: Int = it.indexOf("}")
                    val ip = JSONObject(it.substring(start, end + 1)).get("cip") as String
                    ipStringBuilder.append("网络请求下的IP为:$ip\n")
                    tvIPInfo.text = ipStringBuilder
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@IPActivity, "获取IP失败：${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}