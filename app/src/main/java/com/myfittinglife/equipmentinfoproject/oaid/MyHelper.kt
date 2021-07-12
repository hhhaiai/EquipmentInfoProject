package com.myfittinglife.equipmentinfoproject.oaid

import android.content.Context
import android.util.Log
import com.bun.miitmdid.core.ErrorCode
import com.bun.miitmdid.core.MdidSdkHelper
import com.bun.miitmdid.interfaces.IIdentifierListener
import com.bun.miitmdid.interfaces.IdSupplier

/**
 * @Author LD
 * @Time 2021/3/23 11:20
 * @Describe
 * @Modify
 */
class MyHelper(private val _listener:AppIdsUpdater) : IIdentifierListener {

    /**
     * 进行初始化
     */
    private fun CallFromReflect(cxt: Context): Int {
        return MdidSdkHelper.InitSdk(cxt, true, this)
    }
    fun getDeviceIds(cxt: Context?) {
        val timeb = System.currentTimeMillis()
        // 方法调用
        val nres = CallFromReflect(cxt!!)
        val timee = System.currentTimeMillis()
        val offset = timee - timeb
        Log.i("ceshi", "getDeviceIds: 耗时：$offset")

        when (nres) {
            ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT->{
                //不支持的厂商
                _listener.onError("不支持的厂商")
            }
            ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT -> { //不支持的设备
                _listener.onError("不支持的设备")
            }
            ErrorCode.INIT_ERROR_LOAD_CONFIGFILE -> { //加载配置文件出错
                _listener.onError("加载配置文件出错")
            }
            ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT -> { //不支持的设备厂商
                _listener.onError("不支持的设备厂商")
            }
            ErrorCode.INIT_ERROR_RESULT_DELAY -> { //获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程
                _listener.onError("获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程")
                // TODO: 2021/3/23 时间长没反应怎么回调？？
            }
            ErrorCode.INIT_HELPER_CALL_ERROR -> { //反射调用出错
                _listener.onError("反射调用出错")
            }
        }
    }

    override fun OnSupport(isSupport: Boolean, _supplier: IdSupplier?) {
        if (_supplier == null) {
            _listener.onError("获取到的信息为空")
            return
        }
        val oaid: String = _supplier.oaid
        val vaid: String = _supplier.vaid
        val aaid: String = _supplier.aaid
        val builder = StringBuilder()
        if(!isSupport){
            _listener.onError("不支持OAID")
            return
        }
        builder.append("support: ").append(if (isSupport) "true" else "false").append("\n")
        builder.append("OAID: ").append(oaid).append("\n")
        builder.append("VAID: ").append(vaid).append("\n")
        builder.append("AAID: ").append(aaid).append("\n")
        val idstext = builder.toString()
        _listener.OnIdsAvalid(idstext)
    }
    interface AppIdsUpdater {
        fun OnIdsAvalid( ids: String?)

        fun onError(msg:String)
    }
}