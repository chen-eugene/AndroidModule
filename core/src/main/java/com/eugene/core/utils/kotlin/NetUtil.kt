package com.example.core.utils.kotlin

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

/**
 * 跟网络相关的工具类
 */

class NetUtil private constructor() {


    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    fun isConnected(context: Context): Boolean {

        // API版本23以上时调用此方法进行检测
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val connManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connManager != null) {
                val info = connManager.activeNetworkInfo
                if (info != null && info.isConnected) {
                    return info.state == NetworkInfo.State.CONNECTED
                }
            }
        } else {
            val connManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connManager != null) {
                var info = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                val isWifiConn = info.isConnected

                info = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                val isMobileConn = info.isConnected

                return isWifiConn or isMobileConn
            }
        }// API版本23以下时调用此方法进行检测
        return false
    }

    /**
     * 判断是否是wifi连接
     */
    fun isWifi(context: Context): Boolean {
        val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return false

        return cm.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI

    }

    /**
     * 是否连接移动网络
     *
     * @param context
     * @return
     */
    fun isMobile(context: Context): Boolean {

        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return false

        return connManager.activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE

    }

    /**
     * 判断GPS是否打开
     * ACCESS_FINE_LOCATION权限
     */
    fun isGPSEnable(context: Context): Boolean {

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false

    }


    /**
     * 打开网络设置界面
     */
    fun openSetting(activity: Activity) {
        val intent = Intent("/")
        val cm = ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings")
        intent.component = cm
        intent.action = "android.intent.action.VIEW"
        activity.startActivityForResult(intent, 0)
    }

    /**
     * 获取ip地址
     */
    fun getIp(context: Context): String? {
        val connService = context.getSystemService(Context.CONNECTIVITY_SERVICE) ?: return null
        val connManager = connService as ConnectivityManager
        val info = connManager.activeNetworkInfo

        if (info != null && info.isConnected) {
            // 3/4g网络
            when (info.type) {
                ConnectivityManager.TYPE_MOBILE -> try {
                    val en = NetworkInterface.getNetworkInterfaces()
                    while (en.hasMoreElements()) {
                        val intf = en.nextElement()
                        val enumIpAddr = intf.inetAddresses
                        while (enumIpAddr.hasMoreElements()) {
                            val inetAddress = enumIpAddr.nextElement()
                            if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                return inetAddress.getHostAddress()
                            }
                        }
                    }
                } catch (e: SocketException) {
                    e.printStackTrace()
                }
                ConnectivityManager.TYPE_WIFI -> {
                    //  wifi网络
                    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val wifiInfo = wifiManager.connectionInfo
                    return intIP2StringIP(wifiInfo.ipAddress)
                }
                ConnectivityManager.TYPE_ETHERNET ->
                    // 有限网络
                    return getLocalIp()
            }
        }
        return null

    }

    private fun intIP2StringIP(ip: Int): String {
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }

    /**
     * 获取有限网IP
     */
    private fun getLocalIp(): String {
        try {
            val en = NetworkInterface
                    .getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf
                        .inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: SocketException) {

        }
        return "0.0.0.0"
    }
}
