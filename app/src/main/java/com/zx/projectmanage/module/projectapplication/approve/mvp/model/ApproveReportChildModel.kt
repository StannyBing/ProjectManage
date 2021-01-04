package com.zx.projectmanage.module.projectapplication.approve.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.gt.giscollect.base.NormalList
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.approve.bean.ReportSubListBean

import com.zx.projectmanage.module.projectapplication.approve.mvp.contract.ApproveReportChildContract
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveReportChildModel : BaseModel(), ApproveReportChildContract.Model {
    override fun getPageSubProject(map: Map<String, String>): Observable<NormalList<ReportSubListBean>> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getApproveSubProject(map)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

}