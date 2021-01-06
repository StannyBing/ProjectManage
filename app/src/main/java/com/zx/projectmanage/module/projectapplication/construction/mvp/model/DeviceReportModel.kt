package com.zx.projectmanage.module.projectapplication.construction.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.base.NormalList
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.construction.bean.BaiduGeocoderBean
import com.zx.projectmanage.module.projectapplication.construction.bean.StepStandardBean
import com.zx.projectmanage.module.projectapplication.construction.bean.FileUploadBean

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.DeviceReportContract
import okhttp3.RequestBody
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class DeviceReportModel : BaseModel(), DeviceReportContract.Model {
    override fun baiduGeocoderData(url: String): Observable<BaiduGeocoderBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .geocoder(url)
            .compose(RxSchedulers.io_main())
    }

    override fun stepStandardData(map: HashMap<String, String>): Observable<NormalList<StepStandardBean>> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getStepStandard(map)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun stepDetailData(id: String): Observable<StepStandardBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getStepDetail(id)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun uploadFileData(body: RequestBody): Observable<FileUploadBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .uploadFile(body)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun saveInfoData(body: RequestBody): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .saveDataInfo(body)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun updateInfoData(body: RequestBody): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .updateDataInfo(body)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun deviceDeleteData(id: String): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .deleteDevice(id)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}