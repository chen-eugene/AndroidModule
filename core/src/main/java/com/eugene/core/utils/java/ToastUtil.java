package com.eugene.core.utils.java;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.eugene.core.R;


/**
 * Toast工具类 2017.03.02修改
 *
 * @author WikerYong
 * @version 2012-5-21 下午9:21:01
 */
public class ToastUtil {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static Object synObj = new Object();

    /**
     * Toast发送消息，默认Toast.LENGTH_SHORT
     *
     * @param act
     * @param msg
     */
    public static void showMessage(final Context act, final String msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_LONG
     *
     * @param act
     * @param msg
     */
    public static void showMessageLong(final Context act, final String msg) {
        showMessage(act, msg, Toast.LENGTH_LONG);
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_SHORT
     *
     * @param act
     * @param msg
     */
    public static void showMessage(final Context act, final int msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_LONG
     *
     * @param act
     * @param msg
     */
    public static void showMessageLong(final Context act, final int msg) {
        showMessage(act, msg, Toast.LENGTH_LONG);
    }

    /**
     * Toast发送消息
     *
     * @param act
     * @param msg
     * @param len
     */
    public static void showMessage(final Context act, final int msg,
                                   final int len) {
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        synchronized (synObj) {
                            View view = View.inflate(act, R.layout.toast_bg, null);
                            TextView tvToast = (TextView) view.findViewById(R.id.tv_toast);
                            tvToast.setText(msg);
                            if (toast == null) {
                                toast = new Toast(act);
                                toast.setView(view);
                                toast.setDuration(len);
                            }
                            toast.show();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * Toast发送消息
     *
     * @param act
     * @param msg
     * @param len
     */
    public static void showMessage(final Context act, final String msg,
                                   final int len) {
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        synchronized (synObj) {
                            View view = View.inflate(act, R.layout.toast_bg, null);
                            TextView tvToast = (TextView) view.findViewById(R.id.tv_toast);
                            tvToast.setText(msg);
                            if (toast == null) {
                                toast = new Toast(act);
                                toast.setView(view);
                                toast.setDuration(len);
                            }
                            toast.show();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 关闭当前Toast
     */
    public static void cancelCurrentToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
