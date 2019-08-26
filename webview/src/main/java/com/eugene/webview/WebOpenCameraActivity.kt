package com.eugene.webview

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_web_open_camera.*
import java.io.File

const val REQUEST_EXTERNAL_STORAGE = 1
const val CAPTURE_REQUEST = 123

class WebOpenCameraActivity : AppCompatActivity() {

    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCaptureUri: Uri? = null

    private val PERMISSIONS_STORAGE =
            arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_open_camera)

        //获取权限
        ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)

        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        val setting = mWebView.settings
        //设置缓冲
        val appCacheDir = mWebView.context.applicationContext.getDir("cache", Context.MODE_PRIVATE).path
        setting.setAppCachePath(appCacheDir)
        setting.cacheMode = WebSettings.LOAD_DEFAULT

        setting.defaultTextEncodingName = "UTF-8"
        setting.allowFileAccess = true
        setting.setAppCacheEnabled(true)
        setting.allowContentAccess = true
        setting.allowUniversalAccessFromFileURLs = true
        setting.allowFileAccessFromFileURLs = true

        //自适应屏幕
        setting.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

        //开启 DOM storage API 功能
        setting.domStorageEnabled = true
        //开启js交互
        setting.javaScriptEnabled = true
        // 设置可以支持缩放
        setting.setSupportZoom(true)
        //扩大比例的缩放
        setting.useWideViewPort = true
        setting.loadWithOverviewMode = true
        //把图片加载放在最后来加载渲染
        setting.blockNetworkImage = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        mWebView.webViewClient = CustomWebViewClient()

        mWebView.webChromeClient = ChromeClient()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                mCaptureUri?.let {
                    mFilePathCallback?.onReceiveValue(arrayOf(it))
                }
                return
            } else
                mFilePathCallback?.onReceiveValue(arrayOf())

        }
    }

    inner class CustomWebViewClient : WebViewClient() {

        //拨打电话
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            if (request?.url?.scheme == "tel:") {
                //跳转到拨号界面，同时传递电话号码
                val dialIntent = Intent(Intent.ACTION_DIAL, request.url)
                startActivity(dialIntent)
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

    }

    inner class ChromeClient : WebChromeClient() {

        //显示获取地理位置提示对话框
        //此方法在Android.M及以上，并且访问的链接是https时才会调用，否则会调用系统原生dialog
        override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
            super.onGeolocationPermissionsShowPrompt(origin, callback)
            //展示自定义权限dialog
            val remember = false
            val builder = AlertDialog.Builder(this@WebOpenCameraActivity)
            builder.setTitle("位置信息")//自定义title
            builder.setMessage("允许刷宝短视频获取您的地理位置信息吗？")//自定义文案
                    .setCancelable(true)
                    .setPositiveButton("允许") { _, _ ->
                        callback?.invoke(origin, true, remember)
                    }
                    .setNegativeButton("不允许") { _, _ ->
                        callback?.invoke(origin, false, remember)
                    }
            builder.show()

        }

        //获取本地文件
        override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {

            mFilePathCallback = filePathCallback

            if (fileChooserParams?.acceptTypes?.isNotEmpty() == true) {
                when (fileChooserParams.acceptTypes[0]) {
                    "image/*" -> {
                        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "imageCapture${System.currentTimeMillis()}.jpg")
                        ///文件路径 storage/emulated/0/Android/data/包名/files/Pictures/imageCapture1563956535105.jpg
                        mCaptureUri = file2Uri(file)
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureUri)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivityForResult(intent, CAPTURE_REQUEST)
                        return true
                    }

                    "video/*" -> {
                        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "videoCapture${System.currentTimeMillis()}.mp4")
                        ///文件路径 storage/emulated/0/Android/data/包名/files/Pictures/videoCapture1563956535105.mp4

                        mCaptureUri = file2Uri(file)

                        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureUri)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivityForResult(intent, CAPTURE_REQUEST)
                        return true
                    }
                }

            }
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
        }

        private fun file2Uri(file: File): Uri {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val authority = "$packageName.fileProvider"
                FileProvider.getUriForFile(application, authority, file)
            } else {
                Uri.fromFile(file)
            }
        }
    }


}