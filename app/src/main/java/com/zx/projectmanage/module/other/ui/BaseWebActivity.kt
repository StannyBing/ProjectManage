package com.zx.projectmanage.module.other.ui

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.frame.zxmvp.base.RxBaseActivity
import com.zx.projectmanage.R
import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.other.mvp.contract.BaseWebContract
import com.zx.projectmanage.module.other.mvp.model.BaseWebModel
import com.zx.projectmanage.module.other.mvp.presenter.BaseWebPresenter
import kotlinx.android.synthetic.main.activity_base_web.*


/**
 * Create By admin On 2017/7/11
 * 功能：基础网页
 */
open class BaseWebActivity<T, U> : BaseActivity<BaseWebPresenter, BaseWebModel>(), BaseWebContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, BaseWebActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    private var fileCallback: ValueCallback<Array<Uri>>? = null

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_base_web
    }

    /**
     * 初始化
     */

    @SuppressLint("JavascriptInterface")
    override fun initView(savedInstanceState: Bundle?) {
        web_view.apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(
                JavaScriptinterface(mContext),
                "android"
            );
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

        web_view.setLongClickable(true)
        web_view.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                return true
            }
        })
        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }

    /**
     * 获取webview对象
     */
    fun getWebView(): WebView {
        return web_view
    }

    /**
     * 加载页面
     */
    fun loadUrl(url: String) {
        web_view.loadUrl(url)
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
        web_view.loadUrl("javascript:${methodName}(${params})")

    }

    /**
     * js调用原生
     */
    @SuppressLint("JavascriptInterface")
    fun applyMethod(methodName: String, listener: Any) {
        web_view.addJavascriptInterface(listener, methodName)
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
        if (web_view.canGoBack()) {
            web_view.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        web_view.onPause()
    }

    override fun onResume() {
        super.onResume()
        web_view.onResume()
    }

    class JavaScriptinterface(c: Context) {
        var context: Context

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        fun showToast(ssss: String?) {
            Toast.makeText(context, ssss, Toast.LENGTH_LONG).show()
        }

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        fun getToken(): String {
            return if (ApiConfigModule.COOKIE.isEmpty()) "Basic YXBwOmFwcA==" else "Bearer ${ApiConfigModule.COOKIE}"
        }

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        fun setFinish() {
            val activity = context as Activity
            activity.finish()
        }

        init {
            context = c
        }
    }

}
