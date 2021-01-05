package com.zx.projectmanage.module.projectapplication.construction.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectProcessInfoBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.MacroReportInfoContract

import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class MacroReportInfoModel : BaseModel(), MacroReportInfoContract.Model {
    override fun getProcessInfo(processId: String): Observable<ProjectProcessInfoBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getProcessProjectInfo(processId)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}