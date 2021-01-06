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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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
        val idMapList = arrayListOf<Pair<String, HashMap<String, String?>>>()
        val fileList = arrayListOf<Pair<String, File>>()
        dataList.filter { it.type == DeviceInfoBean.Step_Type }.forEachIndexed { dataIndex, deviceInfoBean ->
            deviceInfoBean.stepInfos.forEachIndexed { index, dataStepInfoBean ->
                //移除示意图
                if (index > 0) {
                    val uuid = UUID.randomUUID().toString()
                    if (dataStepInfoBean.path.startsWith("http")) {
                        //已上传
                        idMapList.add(
                            uuid to hashMapOf(
                                "fileType" to if (dataStepInfoBean.type == DataStepInfoBean.Type.PICTURE) "0" else "1",
                                "attachment" to deviceBean?.postDetails?.get(dataIndex)?.attachmentId,
                                "stepId" to deviceInfoBean.stepInfoBean?.stepId
                            )
                        )
                    } else {
                        //未上传
                        idMapList.add(
                            uuid to hashMapOf(
                                "fileType" to if (dataStepInfoBean.type == DataStepInfoBean.Type.PICTURE) "0" else "1",
                                "attachment" to "",
                                "stepId" to deviceInfoBean.stepInfoBean?.stepId
                            )
                        )
                        fileList.add(uuid to File(dataStepInfoBean.path))
                    }
                }
            }
        }

        uploadFile(dataList, deviceBean, fileList, uploadIndex, idMapList)
//        mModel.uploadFileData(getUploadRequestBody(fileList, uploadIndex))
//            .compose(RxHelper.bindToLifecycle(mView))
//            .apply {
//                for (i in 0 until fileList.size) {
//                    flatMap {
//                        val temp = idMapList.filter { it.first == fileList[uploadIndex].first }
//                        if (temp.isNotEmpty()) {
//                            temp.first().second["attachment"] = it.id
//                        }
//                        uploadIndex++
//                        mModel.uploadFileData(getUploadRequestBody(fileList, uploadIndex))
//                    }
//                }
//            }
//            .flatMap {
//                val temp = idMapList.filter { it.first == fileList[uploadIndex].first }
//                if (temp.isNotEmpty()) {
//                    temp.first().second["attachment"] = it.id
//                }
//                val equipmentId = dataList.first { it.name == "设备ID" }.stringValue
//                val equipmentName = dataList.first { it.name == "设备名称" }.stringValue
//                val detailedId = deviceBean?.detailedId
//                val subProjectId = deviceBean?.subProjectId
//                val standardId = dataList.first { it.name == "规范模板" }.standardBean?.id
//                val postAddr = dataList.first { it.name == "上报位置" }.stringValue
//                val latitude = dataList.first { it.name == "上报位置" }.latitude
//                val longitude = dataList.first { it.name == "上报位置" }.longitude
//                val postDetails = arrayListOf<HashMap<String, String?>>().apply {
//                    idMapList.forEach {
//                        add(it.second)
//                    }
//                }
//                val info = hashMapOf(
//                    "detailedId" to detailedId,
//                    "subProjectId" to subProjectId,
//                    "equipmentId" to equipmentId,
//                    "equipmentName" to equipmentName,
//                    "standardId" to standardId,
//                    "postAddr" to postAddr,
//                    "latitude" to latitude,
//                    "longitude" to longitude,
//                    "postDetails" to postDetails
//                )
//                if (deviceBean?.id == null) {
//                    mModel.saveInfoData(info.toJson2())
//                } else {
//                    mModel.updateInfoData(info.apply {
//                        put("id", deviceBean.id)
//                    }.toJson2())
//                }
//            }
//            .subscribe(object : RxSubscriber<Any>(mView) {
//                override fun _onNext(t: Any?) {
//                    mView.onSaveResult()
//                }
//
//                override fun _onError(code: Int, message: String?) {
//                    mView.handleError(code, message)
//                }
//            })
    }

    private fun uploadFile(
        dataList: List<DeviceInfoBean>,
        deviceBean: DeviceListBean?,
        fileList: List<Pair<String, File>>,
        uploadIndex: Int,
        idMapList: ArrayList<Pair<String, HashMap<String, String?>>>
    ) {
        mModel.uploadFileData(getUploadRequestBody(fileList, uploadIndex))
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<FileUploadBean>(mView) {
                override fun _onNext(it: FileUploadBean?) {
                    val temp = idMapList.filter { it.first == fileList[uploadIndex].first }
                    if (temp.isNotEmpty()) {
                        temp.first().second["attachment"] = it?.id
                    }
                    if (uploadIndex >= fileList.size - 1) {
                        submitInfo(dataList, deviceBean, idMapList)
                    } else {
                        uploadFile(dataList, deviceBean, fileList, uploadIndex + 1, idMapList)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    private fun submitInfo(dataList: List<DeviceInfoBean>, deviceBean: DeviceListBean?, idMapList: ArrayList<Pair<String, HashMap<String, String?>>>) {
        val equipmentId = dataList.first { it.name == "设备ID" }.stringValue
        val equipmentName = dataList.first { it.name == "设备名称" }.stringValue
        val detailedId = deviceBean?.detailedId
        val subProjectId = deviceBean?.subProjectId
        val standardId = dataList.first { it.name == "规范模板" }.standardBean?.id
        val postAddr = dataList.first { it.name == "上报位置" }.stringValue
        val latitude = dataList.first { it.name == "上报位置" }.latitude
        val longitude = dataList.first { it.name == "上报位置" }.longitude
        val postDetails = arrayListOf<HashMap<String, String?>>().apply {
            idMapList.forEach {
                add(it.second)
            }
        }
        val info = hashMapOf(
            "detailedId" to detailedId,
            "subProjectId" to subProjectId,
            "equipmentId" to equipmentId,
            "equipmentName" to equipmentName,
            "standardId" to standardId,
            "postAddr" to postAddr,
            "latitude" to latitude,
            "longitude" to longitude,
            "postDetails" to postDetails
        )
        if (deviceBean?.id == null) {
            mModel.saveInfoData(info.toJson2())
        } else {
            mModel.updateInfoData(info.apply {
                put("id", deviceBean.id)
            }.toJson2())
        }.compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
                    mView.onSaveResult()
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    override fun deleteDevice(id: String) {
        mModel.deviceDeleteData(id)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
                    mView.onDeviceDeleteResult()
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    private fun getUploadRequestBody(dataList: List<Pair<String, File>>, index: Int): UploadRequestBody {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("path", "app")
        builder.addFormDataPart("file", dataList[index].second.name, RequestBody.create(MediaType.parse("multipart/form-data"), dataList[index].second))
        return UploadRequestBody(builder.build(), UploadRequestBody.UploadListener
        { progress, done ->
            mView.showLoading("上传中...(${index + 1}/${dataList.size})", progress)
        })
    }

}