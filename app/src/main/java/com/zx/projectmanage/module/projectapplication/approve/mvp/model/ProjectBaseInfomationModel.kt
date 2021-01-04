package com.zx.projectmanage.module.projectapplication.approve.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.approve.bean.InformationBean

import com.zx.projectmanage.module.projectapplication.approve.mvp.contract.ProjectBaseInfomationContract
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ProjectBaseInfomationModel : BaseModel(), ProjectBaseInfomationContract.Model {
    override fun getProjectInformation(projectId: String): Observable<InformationBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getProjectInformation(projectId)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}