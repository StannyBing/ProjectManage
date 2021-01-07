package com.zx.projectmanage.module.projectapplication.construction.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProcedureOneReportContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProcedureReportContract
import okhttp3.RequestBody
import rx.Observable


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ProcedureOneReportModel : BaseModel(), ProcedureOneReportContract.Model {
    override fun getDeviceList(map: Map<String, String>): Observable<MutableList<DeviceListBean>> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getDeviceList(map)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun postSubmit(body: RequestBody): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .postSubmit(body)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}