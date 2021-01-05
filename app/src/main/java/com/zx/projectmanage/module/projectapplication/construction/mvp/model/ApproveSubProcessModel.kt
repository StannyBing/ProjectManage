package com.zx.projectmanage.module.projectapplication.construction.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.dto.PostAuditDto

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ApproveSubProcessContract
import okhttp3.RequestBody
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveSubProcessModel : BaseModel(), ApproveSubProcessContract.Model {
    override fun getDeviceList(detailedProId: String): Observable<MutableList<DeviceListBean>> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getDeviceList(detailedProId)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun postAuditProcess(body: PostAuditDto): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .auditProcess(body)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}