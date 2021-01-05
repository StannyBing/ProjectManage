package com.zx.projectmanage.module.projectapplication.construction.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.construction.bean.ApproveProcessInfoBean

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ApproveProcessContract
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveProcessModel : BaseModel(), ApproveProcessContract.Model {
    override fun getProcessInfo(projectId: String, subProjectId: String): Observable<MutableList<ApproveProcessInfoBean>> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getApproveProcessProjectInfo(projectId, subProjectId)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}