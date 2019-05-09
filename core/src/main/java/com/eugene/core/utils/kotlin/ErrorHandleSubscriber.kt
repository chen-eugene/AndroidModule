package com.example.core.utils.kotlin

import android.net.ParseException
import android.net.sip.SipErrorCode.SERVER_ERROR
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

const val NOT_FOUND = 404
const val REQUEST_SUCCESS = 200
const val REQUEST_REFUSED = 403
const val REQUEST_REDIRECTED = 307
const val SERVER_ERROR = 500

abstract class ErrorHandleSubscriber<T> : Observer<T> {

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        var msg = "未知错误"
        if (e is UnknownHostException || e is ConnectException) {
            msg = "网络不可用"
        } else if (e is SocketTimeoutException) {
            msg = "请求网络超时"
        } else if (e is HttpException) {
            msg = convertStatusCode(e)
        } else if (e is JsonParseException || e is ParseException || e is JSONException || e is JsonIOException) {
            msg = "数据解析错误"
        }
//        ToastUtil.showMessage(BaseApplication.instance!!, msg)
    }

    private fun convertStatusCode(httpException: HttpException): String {
        return when {
            httpException.code() == SERVER_ERROR -> "服务器发生错误"
            httpException.code() == NOT_FOUND -> "请求地址不存在"
            httpException.code() == REQUEST_REFUSED -> "请求被服务器拒绝"
            httpException.code() == REQUEST_REDIRECTED -> "请求被重定向到其他页面"
            else -> httpException.message()
        }
    }

}