package com.zx.projectmanage.module.projectapplication.approve.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.approve.bean.DeviceListBean

import com.zx.projectmanage.module.projectapplication.approve.mvp.contract.ProcedureReportContract
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ProcedureReportModel : BaseModel(), ProcedureReportContract.Model {

    override fun getDeviceList(map: Map<String, String>): Observable<MutableList<DeviceListBean>> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getDeviceList(map)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

}