package com.zx.projectmanage.module.main.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.frame.zxmvp.http.download.DownInfo
import com.frame.zxmvp.http.download.listener.DownloadOnNextListener
import com.frame.zxmvp.http.download.manager.HttpDownManager
import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.app.ConstStrings
import com.zx.projectmanage.module.main.bean.VersionBean
import com.zx.projectmanage.module.main.mvp.contract.UserContract
import com.zx.zxutils.util.ZXFileUtil
import org.json.JSONObject
import java.io.File
import java.net.URLEncoder


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class UserPresenter : UserContract.Presenter() {

    override fun getVersion() {
        mModel.versionData()
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<VersionBean>(mView) {
                override fun _onNext(t: VersionBean?) {
                    if (t != null) {
                        mView.onVersionResult(t)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    override fun downloadApk(versionBean: VersionBean) {
        var fileName = ""
        var countLength = 0L
        var fileUri = ""
        try {
            val fileObj = JSONObject(versionBean.fileJson)
            fileName = fileObj.getString("fileName")
            fileUri = fileObj.getString("fileUri")
            countLength = fileObj.getLong("fileSize")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val downInfo = DownInfo(
            "file/downloadFileByAbsPath?fileName=${fileName}&fileUri=${URLEncoder.encode(fileUri)}"
        )
        downInfo.countLength = countLength
        downInfo.baseUrl = ApiConfigModule.BASE_IP
        downInfo.savePath = ConstStrings.getApkPath() + fileName
        downInfo.listener = object : DownloadOnNextListener<Any>() {
            override fun onNext(o: Any) {

            }

            override fun onStart() {
                mView.onDownloadProgress(0)
            }

            override fun onComplete(file: File) {
                mView.onApkDownloadResult(file)
                mView.dismissLoading()
            }

            override fun onError(message: String?) {
                mView.showToast(message)
                mView.dismissLoading()
            }

            override fun updateProgress(progress: Int) {
                mView.onDownloadProgress(progress)
            }
        }
        if (ZXFileUtil.isFileExists(ConstStrings.getCachePath() + fileName)) {
            mView.onApkDownloadResult(File(ConstStrings.getCachePath() + fileName))
        } else {
            HttpDownManager.getInstance().startDown(downInfo) { chain ->
                val original = chain.request()
                val request = original.newBuilder()
//                    .header("Cookie", ConstStrings.Cookie)
                    .build()
                chain.proceed(request)
            }
        }
    }

}