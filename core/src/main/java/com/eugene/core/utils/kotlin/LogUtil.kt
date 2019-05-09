package com.example.core.utils.kotlin

import android.util.Log

/**
 * Log统一管理类
 */

object LogUtil {

    /**
     * 日志等级 NOTHING表示不打印日志
     */
    enum class Level {
        VERBOSE, DEBUG, INFO, WARN, ERROR, NOTHING
    }

    var LEVEL = LogUtil.Level.VERBOSE// 打印日志等级，可以在application的onCreate函数里面初始化

    fun v(tag: String, msg: String) {
        if (LEVEL == LogUtil.Level.VERBOSE) {
            Log.v(tag, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (LEVEL == LogUtil.Level.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (LEVEL == LogUtil.Level.INFO) {
            Log.i(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (LEVEL == LogUtil.Level.WARN) {
            Log.w(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (LEVEL == LogUtil.Level.ERROR) {
            Log.e(tag, msg)
        }
    }
}
