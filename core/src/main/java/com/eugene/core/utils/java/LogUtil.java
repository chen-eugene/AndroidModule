package com.eugene.core.utils.java;

import android.util.Log;

/**
 * Log统一管理类
 */

public class LogUtil {


    private LogUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static final Level LEVEL = Level.VERBOSE;// 打印日志等级，可以在application的onCreate函数里面初始化

    /**
     * 日志等级 NOTHING表示不打印日志
     */
    enum Level {
        VERBOSE, DEBUG, INFO, WARN, ERROR, NOTHING;
    }

    public static void v(String tag, String msg) {
        if (LEVEL == Level.VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LEVEL == Level.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LEVEL == Level.INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LEVEL == Level.WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL == Level.ERROR) {
            Log.e(tag, msg);
        }
    }
}
