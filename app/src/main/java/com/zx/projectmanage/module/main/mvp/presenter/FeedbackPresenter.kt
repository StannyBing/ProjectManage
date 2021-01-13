package com.zx.projectmanage.module.main.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.frame.zxmvp.http.upload.UploadRequestBody
import com.zx.projectmanage.app.toJson2
import com.zx.projectmanage.module.main.mvp.contract.FeedbackContract
import com.zx.projectmanage.module.projectapplication.construction.bean.FileUploadBean
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class FeedbackPresenter : FeedbackContract.Presenter() {
    var idList: MutableList<String> = ArrayList()
    private fun uploadFile(
        content: String,
        fileList: MutableList<String>,
        uploadIndex: Int,
        type: Int
    ) {
        mModel.uploadFileData(getUploadRequestBody(fileList, uploadIndex))
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<FileUploadBean>(mView) {
                override fun _onNext(it: FileUploadBean?) {
                    idList.add(it?.id.toString())
                    if (uploadIndex >= fileList.size - 1) {
                        submitInfo(content, idList, type)
                    } else {
                        uploadFile(content, fileList, uploadIndex + 1, type)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    private fun getUploadRequestBody(dataList: MutableList<String>, index: Int): UploadRequestBody {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("path", "app")
        builder.addFormDataPart("file", File(dataList[index]).name, RequestBody.create(MediaType.parse("multipart/form-data"), File(dataList[index])))
        return UploadRequestBody(builder.build(), UploadRequestBody.UploadListener
        { progress, done ->
            mView.showLoading("上传中...(${index + 1}/${dataList.size})", progress)
        })
    }

    override fun saveDataInfo(content: String, imageList: MutableList<String>, type: Int) {
        var uploadIndex: Int = 0
        if (imageList.isNotEmpty()) {
            uploadFile(content, imageList, uploadIndex, type)
        } else {
            submitInfo(content, null, type)
        }
    }

    private fun submitInfo(content: String, idList: List<String>? = null, type: Int? = 0) {

        val annexList: MutableList<HashMap<String, String>> = ArrayList()
        idList?.forEachIndexed { index, s ->

            if (index == idList.size - 1 && type == 1) {
                annexList.add(
                    hashMapOf(
                        "fileId" to s,
                        "fileType" to "1"
                    )
                )
            } else {
                annexList.add(
                    hashMapOf(
                        "fileId" to s,
                        "fileType" to "0"
                    )
                )
            }

        }

        val info = hashMapOf(
            "content" to content,
            "annexList" to annexList
        )
        mModel.saveInfoData(info.toJson2())
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
                    mView.onSaveResult()
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

}