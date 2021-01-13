package com.zx.projectmanage.module.other.ui

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.frame.zxmvp.base.RxBaseActivity
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity

import com.zx.projectmanage.module.other.mvp.contract.BaseWebContract
import com.zx.projectmanage.module.other.mvp.model.BaseWebModel
import com.zx.projectmanage.module.other.mvp.presenter.BaseWebPresenter
import kotlinx.android.synthetic.main.activity_base_web.*


/**
 * Create By admin On 2017/7/11
 * 功能：基础网页
 */
open class BaseWeb1Activity<T, U> : BaseActivity<BaseWebPresenter, BaseWebModel>(), BaseWebContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, BaseWeb1Activity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    private var webView: WebView? = null

    private var fileCallback: ValueCallback<Array<Uri>>? = null

    fun initWebView(webView1: WebView){
        this.webView =webView1
        webView?.apply {
            settings.javaScriptEnabled = true
            scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
            webViewClient = WebViewClient()
            webChromeClient = object : WebChromeClient() {
                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    fileCallback = filePathCallback
                    val i = Intent(Intent.ACTION_GET_CONTENT)
                    i.addCategory(Intent.CATEGORY_OPENABLE)
                    i.type = "image/*"
                    startActivityForResult(Intent.createChooser(i, "Image Chooser"), 0x01)
                    return true
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (pb_web_loading != null) {
                        pb_web_loading.progress = newProgress
                        pb_web_loading.solidColor
                        if (pb_web_loading.progress == 0 || pb_web_loading.progress == 100) {
                            pb_web_loading.visibility = View.GONE
                        } else {
                            pb_web_loading.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }
    /**
     * 加载页面
     */
    fun loadUrl(url: String) {
        webView?.loadUrl(url)
    }

    /**
     * 调用js方法
     */
    fun loadMethod(methodName: String, vararg info: Any) {
        var params = StringBuilder()
        info.forEachIndexed { index, it ->
            params.append("'")
            params.append(it)
            params.append("'")
            if (index < info.size - 1) params.append(",")
        }
        webView?.loadUrl("javascript:${methodName}(${params})")

    }

    /**
     * js调用原生
     */
    @SuppressLint("JavascriptInterface")
    fun applyMethod(methodName: String, listener: Any) {
        webView?.addJavascriptInterface(listener, methodName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x01) {
            if (null == fileCallback) return
            val result =
                if (data == null || resultCode != RxBaseActivity.RESULT_OK) null else data.data
            onActivityResultAboveL(requestCode, resultCode, data)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onActivityResultAboveL(requestCode: Int, resultCode: Int, intent: Intent?) {
        var results = arrayListOf<Uri>()
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                val dataString = intent.dataString
                val clipData = intent.clipData
                if (clipData != null) {
                    for (i in 0 until clipData.itemCount) {
                        val item = clipData.getItemAt(i)
                        results.add(item.uri)
                    }
                }
                if (dataString != null)
                    results = arrayListOf(Uri.parse(dataString))
            }
        }
        fileCallback!!.onReceiveValue(results.toArray() as Array<Uri>?)
        fileCallback = null
    }

    override fun onBackPressed() {
        if (webView?.canGoBack() ==true) {
            webView?.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        webView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }

    override fun getLayoutId(): Int {
        return  0
    }

}
