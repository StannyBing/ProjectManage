package com.zx.projectmanage.module.projectapplication.construction.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.construction.bean.ProcessProgressBean

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProjectProgressContract
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ProjectProgressModel : BaseModel(), ProjectProgressContract.Model {
    override fun getProcessProgress(detailedProId: String): Observable<MutableList<ProcessProgressBean>> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getProcessProgress(detailedProId)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}