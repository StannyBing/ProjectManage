package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import android.os.Environment
import android.util.Log
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.frame.zxmvp.http.download.DownInfo
import com.frame.zxmvp.http.download.listener.DownloadOnNextListener
import com.frame.zxmvp.http.download.manager.HttpDownManager
import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.app.ConstStrings
import com.zx.projectmanage.module.projectapplication.construction.bean.FileInfoBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.DocumentContract
import com.zx.zxutils.util.ZXFileUtil
import java.io.File


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class DocumentPresenter : DocumentContract.Presenter() {


    var path = Environment.getExternalStorageDirectory().absolutePath
    override fun getFile(id: String, fileName: String, countLength: Long) {
        ConstStrings
        val downUrl: String = ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=${id}"
        val downInfo = DownInfo(downUrl)
        downInfo.baseUrl = ApiConfigModule.BASE_IP
        val savePath = "$path/projectManage/$fileName"
        downInfo.savePath = savePath
        downInfo.countLength = countLength
        downInfo.listener = object : DownloadOnNextListener<Any?>() {
            override fun onStart() {
                mView.showLoading("正在下载中，请稍后...", 0)
                //                ZXDialogUtil.showLoadingDialog(mContext, "正在下载中，请稍后...", 0);
            }

            override fun onNext(t: Any?) {

            }

            override fun onComplete(file: File?) {
                mView.onFileDownloadResult(file)
                mView.dismissLoading()
                //                ZXDialogUtil.dismissLoadingDialog();
            }

            override fun updateProgress(progress: Int) {
                mView.showLoading("正在下载中，请稍后...", progress)
            }

            override fun onError(message: String?) {
                super.onError(message)
                Log.e("xx", "onError: " )
            }


        }
        if (ZXFileUtil.isFileExists(savePath)) {
            mView.onFileDownloadResult(File(savePath))
        } else {
            HttpDownManager.getInstance().startDown(downInfo)
        }
    }

    override fun getFileInfo(id: String) {
        mModel.getFileInfo(id)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<FileInfoBean>(mView) {
                override fun _onNext(t: FileInfoBean?) {
                    mView.onFileInfoResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

}