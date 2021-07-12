package com.myfittinglife.equipmentinfoproject.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.StringBuilder

/**
 * @Author LD
 * @Time 2021/7/2 9:07
 * @Describe 申请权限相关的工具类
 * @Modify
 */
object PermissionUtil {

    /**
     * 验证是否有权限，没有则申请
     */
     fun requestPermission(requestCode: Int, permissionList: List<String>, context: Context): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //没有同意需要申请的权限
            val requestPermissionList = mutableListOf<String>()
            for (permission in permissionList) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionList.add(permission)
                }
            }
            if (requestPermissionList.size > 0) {
                ActivityCompat.requestPermissions(
                    context as Activity,
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

    /**
     *验证权限申请的结果
     */
     fun verifyResult(grantResults: IntArray,context: Context): Boolean {
        if (grantResults.isNotEmpty()) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "必须同意所有权限才能使用该功能", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            return true
        } else {
            Toast.makeText(context, "发生未知错误", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}