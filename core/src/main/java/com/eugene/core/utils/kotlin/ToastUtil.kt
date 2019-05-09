package com.example.core.utils.kotlin


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.core.R


/**
 * Toast工具类 2017.03.02修改
 *
 * @author WikerYong
 * @version 2012-5-21 下午9:21:01
 */
object ToastUtil {
    private val handler = Handler(Looper.getMainLooper())
    private var toast: Toast? = null
    private val synObj = Any()

    /**
     * Toast发送消息，默认Toast.LENGTH_SHORT
     *
     * @param act
     * @param msg
     */
    fun showMessage(act: Context, msg: String) {
        showMessage(act, msg, Toast.LENGTH_SHORT)
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_SHORT
     *
     * @param act
     * @param msg
     */
    fun showMessage(act: Context, msg: Int) {
        showMessage(act, msg, Toast.LENGTH_SHORT)
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_LONG
     *
     * @param act
     * @param msg
     */
    fun showMessageLong(act: Context, msg: String) {
        showMessage(act, msg, Toast.LENGTH_LONG)
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_LONG
     *
     * @param act
     * @param msg
     */
    fun showMessageLong(act: Context, msg: Int) {
        showMessage(act, msg, Toast.LENGTH_LONG)
    }

    /**
     * Toast发送消息
     *
     * @param act
     * @param msg
     * @param len
     */
    fun showMessage(act: Context, msg: Int,
                    len: Int = Toast.LENGTH_SHORT) {
        Thread(Runnable {
            handler.post {
                synchronized(synObj) {
                    val view = View.inflate(act, R.layout.toast_bg, null)
                    val tvToast = view.findViewById<View>(R.id.tv_toast) as TextView
                    tvToast.setText(msg)
                    if (toast == null) {
                        toast = Toast(act)
                        toast!!.view = view
                        toast!!.duration = len
                    }
                    toast!!.show()
                }
            }
        }).start()
    }

    /**
     * Toast发送消息
     *
     * @param act
     * @param msg
     * @param len
     */
    fun showMessage(act: Context, msg: String,
                    len: Int = Toast.LENGTH_SHORT) {
        Thread(Runnable {
            handler.post {
                synchronized(synObj) {
                    val view = View.inflate(act, R.layout.toast_bg, null)
                    val tvToast = view.findViewById<View>(R.id.tv_toast) as TextView
                    tvToast.text = msg
                    if (toast == null) {
                        toast = Toast(act)
                    }
                    toast!!.view = view
                    toast!!.duration = len
                    toast!!.show()
                }
            }
        }).start()
    }

    /**
     * 关闭当前Toast
     */
    fun cancelCurrentToast() {
        if (toast != null) {
            toast!!.cancel()
        }
    }
}


