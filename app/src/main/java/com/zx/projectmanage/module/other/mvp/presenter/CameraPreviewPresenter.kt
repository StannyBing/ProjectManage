package com.zx.projectmanage.module.other.mvp.presenter

import com.frame.zxmvp.http.download.DownInfo
import com.frame.zxmvp.http.download.listener.DownloadOnNextListener
import com.frame.zxmvp.http.download.manager.HttpDownManager
import com.zx.projectmanage.app.ConstStrings
import com.zx.projectmanage.module.other.mvp.contract.CameraPreviewContract
import com.zx.zxutils.util.ZXDialogUtil
import com.zx.zxutils.util.ZXFileUtil
import java.io.File


/**
 * Create By XB
 * 功能：
 */
class CameraPreviewPresenter : CameraPreviewContract.Presenter() {

    override fun downloadFile(name: String, downUrl: String) {
        val downInfo = DownInfo(downUrl)
//        if (!File(ConstStrings.getCachePath() + "Download/").exists()){
//
//        }
        val savePath = ConstStrings.getCachePath() + name
//        downInfo.baseUrl = ApiConfigModule.BASE_IP
        downInfo.savePath = savePath
        downInfo.listener = object : DownloadOnNextListener<Any>() {
            override fun onNext(o: Any) {
                ZXDialogUtil.dismissLoadingDialog()
            }

            override fun onStart() {
                ZXDialogUtil.showLoadingDialog(mContext, "正在下载中，请稍后...", 0)
            }

            override fun onComplete(file: File) {
                mView.onFileDownloadResult(file)
                ZXDialogUtil.dismissLoadingDialog()
            }

            override fun updateProgress(progress: Int) {
                ZXDialogUtil.showLoadingDialog(mContext, "正在下载中，请稍后...", progress)
            }
        }
        if (ZXFileUtil.isFileExists(savePath)) {
            mView.onFileDownloadResult(File(savePath))
        } else {
            HttpDownManager.getInstance().startDown(downInfo)
        }
    }
}