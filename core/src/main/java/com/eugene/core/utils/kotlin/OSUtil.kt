package com.example.core.utils.kotlin

import android.content.Context
import android.content.pm.PackageManager

object OSUtil {

    /**
     * 获取版本号
     */
    fun getVersionCode(context: Context): Int {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 获取指定包名应用的版本号
     *
     * @param context
     * @param packageName
     * @return
     */
    fun getVersionCode(context: Context, packageName: String): Int {
        return try {
            context.packageManager
                    .getPackageInfo(packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    fun getVersionName(context: Context): String {
        return try {
            context.packageManager
                    .getPackageInfo(context.packageName,
                            0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }


}