package com.zx.projectmanage.module.projectapplication.construction.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.bean.StepStandardBean

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.DeviceAuditContract
import okhttp3.RequestBody
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class DeviceAuditModel : BaseModel(), DeviceAuditContract.Model {
    override fun rejectData(body: RequestBody): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .doDeviceReject(body)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun passData(body: RequestBody): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .doDevicePass(body)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun deviceDetailData(standardProId: String, standardId: String): Observable<DeviceListBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getDeviceDetail(standardProId, standardId)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


    override fun stepDetailData(id: String): Observable<StepStandardBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getStepDetail(id)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}