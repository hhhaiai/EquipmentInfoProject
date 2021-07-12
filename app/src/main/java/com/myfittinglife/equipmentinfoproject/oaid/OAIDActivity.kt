package com.myfittinglife.equipmentinfoproject.oaid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bun.miitmdid.core.ErrorCode
import com.bun.miitmdid.core.MdidSdkHelper
import com.bun.miitmdid.interfaces.IIdentifierListener
import com.bun.miitmdid.interfaces.IdSupplier
import com.myfittinglife.equipmentinfoproject.R
import kotlinx.android.synthetic.main.activity_o_a_i_d.*

/**
@Author LD
@Time 2021/6/25 14:36
@Describe 获取OAID
@Modify
 */
class OAIDActivity : AppCompatActivity(), IIdentifierListener {

    companion object {
        const val TAG = "ceshi"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_a_i_d)

        //获取OAID信息
        btnOAIDInfo.setOnClickListener {
            tvInfo.text = ""
            getOAID()
        }
        //工具类的方式调用
        btnOAIDUtil.setOnClickListener {
            tvInfo.text = ""

            //以下回调均在非主线程中
            var myHelper = MyHelper(object : MyHelper.AppIdsUpdater {
                override fun OnIdsAvalid(ids: String?) {
                    Log.i(TAG, "OnIdsAvalid: $ids")
                    runOnUiThread {
                        tvInfo.text = ids
                    }
                }

                override fun onError(msg: String) {
                    Log.i(TAG, "onError: $msg")
                    runOnUiThread {
                        tvInfo.text = msg
                    }
                }
            })
            myHelper.getDeviceIds(this)
        }
    }

    private fun getOAID() {
        //初始化
        var error = MdidSdkHelper.InitSdk(this, true, this)
        when (error) {
            ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT -> {
                //不支持的设备
                Log.i(TAG, "getOAID: 不支持的设备")
                tvInfo.text = "不支持的设备"
            }
            ErrorCode.INIT_ERROR_LOAD_CONFIGFILE -> {
                //加载配置文件出错
                Log.i(TAG, "getOAID: 加载配置文件出错")
                tvInfo.text = "加载配置文件出错"
            }
            ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT -> {
                //不支持的设备厂商
                Log.i(TAG, "getOAID: 不支持的设备厂商")
                tvInfo.text = "不支持的设备厂商"
            }
            ErrorCode.INIT_ERROR_RESULT_DELAY -> {
                //获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程
                Log.i(TAG, "getOAID: 获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程")

            }
            ErrorCode.INIT_HELPER_CALL_ERROR -> {
                //反射调用出错
                Log.i(TAG, "getOAID: 反射调用出错")
                tvInfo.text = "反射调用出错"

            }
        }
    }


    /**
     * 10.0 小米华为可以
     * 9.0 googlepix回调里获取不到(这是个耗时的操作)
     */
    override fun OnSupport(isSupport: Boolean, _supplier: IdSupplier?) {

        if (_supplier == null) {
            runOnUiThread {
                tvInfo.text = "获取到的配置信息为空"
            }
            Log.i(TAG, "OnSupport: 获取到的信息为空")
            return
        }
        //关键用这个
        val oaid: String = _supplier.oaid
        val vaid: String = _supplier.vaid
        val aaid: String = _supplier.aaid
        val builder = StringBuilder()
        builder.append("support: ").append(if (isSupport) "true" else "false").append("\n")
        builder.append("OAID: ").append(oaid).append("\n")
        builder.append("VAID: ").append(vaid).append("\n")
        builder.append("AAID: ").append(aaid).append("\n")
        val idstext = builder.toString()
        runOnUiThread {
            tvInfo.text = idstext

        }
        Log.i(TAG, "OnSupport: 获取到的信息为：$idstext")
    }
}