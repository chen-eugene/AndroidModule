package com.example.core.utils.kotlin

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat

object CrashHandler : Thread.UncaughtExceptionHandler {

    val TAG = this::class.java.name

    /**
     * 系统默认的UncaughtException处理类
     */
    private var defHandler: Thread.UncaughtExceptionHandler? = null

    private lateinit var context: Application

    fun init(context: Application) {
        CrashHandler.context = context
        //获取系统默认的UncaughtException处理器
        defHandler = Thread.getDefaultUncaughtExceptionHandler()
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 捕获异常
     */
    override fun uncaughtException(t: Thread?, e: Throwable?) {

    }

    private fun handleException(ex: Throwable) {

        ToastUtil.showMessage(context, "很抱歉,程序出现异常,即将退出.")

        save2File(ex)
    }

    /**
     * 收集设备信息
     */
    private fun collectDeviceInfo(): Map<String, String> {

        val infoMap = mutableMapOf<String, String>()
        val pm = context.packageManager
        val pi = pm.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)

        val versionName = pi?.versionName ?: "null"
        val versionCode = pi?.versionCode?.toString() ?: "null"

        infoMap["versionName"] = versionName
        infoMap["versionCode"] = versionCode

        val fields = Build::class.java.declaredFields
        fields.forEach {
            try {
                it.isAccessible = true
                infoMap[it.name] = it[null].toString()
                LogUtil.d(TAG, it.name + ":" + it[null])
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return infoMap
    }

    /**
     * 保存到文件中
     */
    private fun save2File(ex: Throwable): String {

        val infoMap = collectDeviceInfo()
        val sb = StringBuffer()
        infoMap.entries.forEach {
            sb.append(it.key + "=" + it.value + "\n")
        }

        val writer = StringWriter()
        val printer = PrintWriter(writer)
        ex.printStackTrace(printer)

        var cause = ex.cause
        while (cause != null) {
            cause.printStackTrace(printer)
            cause = cause.cause
        }
        printer.close()

        val result = writer.toString()
        sb.append(result)

        val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
        val time = formatter.format(System.currentTimeMillis())

        try {
            val fileName = "crash_$time.log"
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val path = Environment.getExternalStorageDirectory().path + "/crash/"
                val dir = File(path)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val fos = FileOutputStream(path + fileName)
                fos.write(sb.toString().toByteArray())
                fos.close()
            }
            return fileName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


}
















