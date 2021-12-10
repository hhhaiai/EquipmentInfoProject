package com.myfittinglife.equipmentinfoproject.build

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.myfittinglife.equipmentinfoproject.R
import kotlinx.android.synthetic.main.activity_build.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

/**
 @Author LD
 @Time 2021/12/9 15:54
 @Describe Build相关属性
 @Modify
*/
class BuildActivity : AppCompatActivity() {

    companion object{
        const val TAG ="ceshi_Build"
    }
    private val msg = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_build)

//        msg.append("：${}\n")

        msg.append("Build.VERSION.RELEASE：${Build.VERSION.RELEASE}\n")
        msg.append("Build.VERSION.SDK_INT：${Build.VERSION.SDK_INT}\n")
        msg.append("Build.VERSION.SDK：${Build.VERSION.SDK}\n")
        msg.append("Build.VERSION.BASE_OS：${Build.VERSION.BASE_OS}\n")
        msg.append("Build.VERSION.CODENAME：${Build.VERSION.CODENAME}\n")
        msg.append("Build.VERSION.INCREMENTAL：${Build.VERSION.INCREMENTAL}\n")
        msg.append("Build.VERSION.PREVIEW_SDK_INT：${Build.VERSION.PREVIEW_SDK_INT}\n")
        msg.append("Build.VERSION.SECURITY_PATCH：${Build.VERSION.SECURITY_PATCH}\n")
        msg.append("\n")
        msg.append("Build.MODEL：${Build.MODEL}\n")
        msg.append("Build.BRAND：${Build.BRAND}\n")
        msg.append("Build.DEVICE：${Build.DEVICE}\n")
        msg.append("Build.BOARD：${Build.BOARD}\n")
        msg.append("Build.PRODUCT：${Build.PRODUCT}\n")
        msg.append("Build.HARDWARE：${Build.HARDWARE}\n")
        msg.append("Build.SERIAL：${Build.SERIAL}\n")
        msg.append("Build.MANUFACTURER：${Build.MANUFACTURER}\n")
        msg.append("\n")
        msg.append("Build.TYPE：${Build.TYPE}\n")
        msg.append("Build.USER：${Build.USER}\n")
        msg.append("Build.HOST：${Build.HOST}\n")
        msg.append("Build.TAGS：${Build.TAGS}\n")
        msg.append("Build.ID：${Build.ID}\n")
        for (i in Build.SUPPORTED_ABIS){
            msg.append("Build.SUPPORTED_ABIS：${i}\n")
        }
        msg.append("Build.DISPLAY：${Build.DISPLAY}\n")
        msg.append("Build.BOOTLOADER：${Build.BOOTLOADER}\n")
        msg.append("Build.getRadioVersion()：${Build.getRadioVersion()}\n")
        for(i in Build.SUPPORTED_32_BIT_ABIS){
            msg.append("Build.SUPPORTED_32_BIT_ABIS：${i}\n")
        }
        for(i in Build.SUPPORTED_64_BIT_ABIS){
            msg.append("Build.SUPPORTED_64_BIT_ABIS：${i}\n")
        }
        msg.append("Build.FINGERPRINT：${Build.FINGERPRINT}\n")
        msg.append("Build.TIME：${Build.TIME}\n")
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val time = simpleDateFormat.format(Date(Build.TIME))
        msg.append("Build.TIME：${time}\n")
//        msg.append("：${}$\n")
//        msg.append("：${}$\n")
        tvInfo.text=msg


        //------------------VERSION相关
        //安卓系统版本
        Log.i(TAG, "onCreate: Build.VERSION.RELEASE--------${Build.VERSION.RELEASE}")
        //运行软件的sdk版本，除非ota升级，否则设备运行时是不会改变的
        Log.i(TAG, "onCreate: Build.VERSION.SDK_INT--------${Build.VERSION.SDK_INT}")
        //编译时sdk版本
        Log.i(TAG, "onCreate: Build.VERSION.SDK--------${Build.VERSION.SDK}")
        //产品构建所基于的操作系统
        Log.i(TAG, "onCreate: Build.VERSION.BASE_OS--------${Build.VERSION.BASE_OS}")
        //当前的开发代号，如果是正式版则是REL
        Log.i(TAG, "onCreate: Build.VERSION.CODENAME--------${Build.VERSION.CODENAME}")
        //当前系统的版本(底层代码使用的值)
        Log.i(TAG, "onCreate: Build.VERSION.INCREMENTAL--------${Build.VERSION.INCREMENTAL}")
        //永远是0
        Log.i(TAG, "onCreate: Build.VERSION.PREVIEW_SDK_INT--------${Build.VERSION.PREVIEW_SDK_INT}")
        //有些手机报错：java.lang.NoSuchFieldError: No static field RELEASE_OR_CODENAME of type Ljava/lang/String
        //可能是安卓系统版本号或者如果不是正式版则是开发代号CODENAME
//        Log.i(TAG, "onCreate: Build.VERSION.RELEASE_OR_CODENAME--------${Build.VERSION.RELEASE_OR_CODENAME}")
        //最近安全补丁日期
        Log.i(TAG, "onCreate: Build.VERSION.SECURITY_PATCH--------${Build.VERSION.SECURITY_PATCH}")
        //------------------------------------常用的

        //手机型号
        Log.i(TAG, "onCreate: Build.MODEL--------${Build.MODEL}")
        //设备品牌
        Log.i(TAG, "onCreate: Build.BRAND--------${Build.BRAND}")
        //设备名
        Log.i(TAG, "onCreate: Build.DEVICE--------${Build.DEVICE}")
        //主板名称
        Log.i(TAG, "onCreate: Build.BOARD--------${Build.BOARD}")
        //手机制造商
        Log.i(TAG, "onCreate: Build.PRODUCT--------${Build.PRODUCT}")
        //硬件名称，来自内核命令行或proc
        Log.i(TAG, "onCreate: Build.HARDWARE--------${Build.HARDWARE}")
        //硬件序列号，但是始终设置为unknown，已过时，被替换为Build.getSerial()，需要系统权限
        Log.i(TAG, "onCreate: Build.SERIAL--------${Build.SERIAL}")
        //硬件制造商
        Log.i(TAG, "onCreate: Build.MANUFACTURER--------${Build.MANUFACTURER}")

        //-----------------------------不常用
        //builder类型
        Log.i(TAG, "onCreate: Build.TYPE--------${Build.TYPE}")
        //系统用户名
        Log.i(TAG, "onCreate: Build.USER--------${Build.USER}")
        //系统主机名
        Log.i(TAG, "onCreate: Build.HOST--------${Build.HOST}")
        //描述build的标签
        Log.i(TAG, "onCreate: Build.TAGS--------${Build.TAGS}")
        //修订版本编号
        Log.i(TAG, "onCreate: Build.ID--------${Build.ID}")
        //Build.CPU_ABI、Build.CPU_ABI2过时，被Build.SUPPORTED_ABIS替代
        //支持的ABI列表
        for (i in Build.SUPPORTED_ABIS){
            Log.i(TAG, "onCreate: Build.SUPPORTED_ABIS--------${i}")
        }
        //用于向用户显示的构建 ID 字符串？(对接文档称显示屏参数)
        Log.i(TAG, "onCreate: Build.DISPLAY--------${Build.DISPLAY}")
        //系统的引导加载程序版本号
        Log.i(TAG, "onCreate: Build.BOOTLOADER--------${Build.BOOTLOADER}")
        //Build.RADIO已过时，被Build.getRadioVersion()替代
        //返回无线电固件版本号，可能返回null，因为收音机未打开
        Log.i(TAG, "onCreate: Build.getRadioVersion()--------${Build.getRadioVersion()}")
        //枚举当前已知的SDK版本号...
        Log.i(TAG, "onCreate: Build.VERSION_CODES()--------${Build.VERSION_CODES.M}")
        //支持的32位ABI列表
        for(i in Build.SUPPORTED_32_BIT_ABIS){
            Log.i(TAG, "onCreate: Build.SUPPORTED_32_BIT_ABIS--------${i}")
        }
        //支持的64为ABI列表
        for(i in Build.SUPPORTED_64_BIT_ABIS){
            Log.i(TAG, "onCreate: Build.SUPPORTED_64_BIT_ABIS--------${i}")
        }
        //唯一标识此build的唯一参数，不要解析它
        Log.i(TAG, "onCreate: Build.FINGERPRINT--------${Build.FINGERPRINT}")
        //机器生产的时间
        Log.i(TAG, "onCreate: Build.TIME--------${Build.TIME}")

    }
}