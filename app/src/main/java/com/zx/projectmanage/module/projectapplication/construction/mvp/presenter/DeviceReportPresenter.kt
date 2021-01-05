package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import android.location.Location
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.frame.zxmvp.http.upload.UploadRequestBody
import com.zx.projectmanage.app.toJson2
import com.zx.projectmanage.base.NormalList
import com.zx.projectmanage.module.projectapplication.construction.bean.*
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.DeviceReportContract
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class DeviceReportPresenter : DeviceReportContract.Presenter() {
    override fun doGeocoder(location: Location) {
        val url = "https://apis.map.qq.com/ws/geocoder/v1/?location=${location.latitude},${location.longitude}&key=UYZBZ-P4RE4-WWBUC-XJ3JG-QJPST-IPB26"
        mModel.baiduGeocoderData(url)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<BaiduGeocoderBean>(mView) {
                override fun _onNext(t: BaiduGeocoderBean?) {
                    if (t != null) {
                        mView.onGeocoderResult(location, t)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }

            })
    }

    override fun getStepStandard(map: HashMap<String, String>) {
        mModel.stepStandardData(map)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<NormalList<StepStandardBean>>(mView) {
                override fun _onNext(t: NormalList<StepStandardBean>?) {
                    t?.records?.let { mView.onStepStandardResult(it) }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    override fun getStepDetail(id: String) {
        mModel.stepDetailData(id)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<StepStandardBean>(mView) {
                override fun _onNext(t: StepStandardBean?) {
                    if (t != null) {
                        mView.onStepDetailResult(t)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    override fun saveDataInfo(
        dataList: List<DeviceInfoBean>,
        deviceBean: DeviceListBean?
    ) {
        var uploadIndex = 0
        val fileList = arrayListOf<File>()
        val uploadIdsList = arrayListOf<String>()
        dataList.forEach {
            it.stepInfos.forEachIndexed { index, dataStepInfoBean ->
                if (index > 0) {
                    fileList.add(File(dataStepInfoBean.path))
                }
            }
        }
        mModel.uploadFileData(getUploadRequestBody(fileList, uploadIndex))
            .compose(RxHelper.bindToLifecycle(mView))
            .apply {
                for (i in 0..(dataList.size - 2)) {
                    flatMap {
                        uploadIdsList.add(it.id)
                        uploadIndex++
                        mModel.uploadFileData(getUploadRequestBody(fileList, uploadIndex))
                    }
                }
            }
            .flatMap {
                uploadIdsList.add(it.id)
                if (uploadIndex < fileList.size - 1) {
                    uploadIndex++
                    return@flatMap mModel.uploadFileData(getUploadRequestBody(fileList, uploadIndex))
                }
                return@flatMap mModel.uploadFileData(getUploadRequestBody(fileList, uploadIndex))
            }
            .flatMap {
                uploadIdsList.add(it.id)
                val equipmentId = dataList.first { it.name == "设备ID" }.stringValue
                val equipmentName = dataList.first { it.name == "设备名称" }.stringValue
                val detailedId = deviceBean?.detailedId
                val subProjectId = deviceBean?.subProjectId
                val standardId = dataList.first { it.name == "规范模板" }.standardBean?.id
                val postAddr = dataList.first { it.name == "上报位置" }.stringValue
                val latitude = dataList.first { it.name == "上报位置" }.latitude
                val longitude = dataList.first { it.name == "上报位置" }.longitude
                val postDetails = arrayListOf<HashMap<String, String>>().apply {
                    var fileIndex = 0
                    dataList.forEach {
                        it.stepInfos.forEachIndexed { index, dataStepInfoBean ->
                            if (index > 0) {
                                add(
                                    hashMapOf(
                                        "attachment" to uploadIdsList[fileIndex],
                                        "fileType" to if (dataStepInfoBean.type == DataStepInfoBean.Type.PICTURE) "0" else "1",
                                        "stepId" to (it.standardBean?.id ?: "")
                                    )
                                )
                                fileIndex++
                            }
                        }
                    }
                }
                mModel.saveInfoData(
                    hashMapOf(
                        "detailedId" to detailedId,
                        "subProjectId" to subProjectId,
                        "equipmentId" to equipmentId,
                        "equipmentName" to equipmentName,
                        "standardId" to standardId,
                        "postAddr" to postAddr,
                        "latitude" to latitude,
                        "longitude" to longitude,
                        "postDetails" to postDetails
                    ).toJson2()
                )
            }
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
                    mView.onSaveResult()
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    private fun getUploadRequestBody(dataList: List<File>, index: Int): UploadRequestBody {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("path", "app")
        builder.addFormDataPart("file", dataList[index].name, RequestBody.create(MediaType.parse("multipart/form-data"), dataList[index]))
        return UploadRequestBody(builder.build(), UploadRequestBody.UploadListener
        { progress, done ->
            mView.showLoading("上传中...(${index + 1}/${dataList.size})", progress)
        })
    }

}