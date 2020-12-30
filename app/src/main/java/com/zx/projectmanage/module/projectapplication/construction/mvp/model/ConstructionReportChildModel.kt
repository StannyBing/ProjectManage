package com.zx.projectmanage.module.projectapplication.construction.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.basebean.BaseRespose
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportChildContract
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ConstructionReportChildModel : BaseModel(), ConstructionReportChildContract.Model {
    override fun getPageSubProject(
        districtCode: String?,
        subProjectName: String?,
        pageNo: Int?,
        pageSize: Int?,
        status: Int?,
        projectId: String,
        tenders: Int?
    ): Observable<ReportListBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getPageSubProject(districtCode, subProjectName, pageNo, pageSize, status, projectId, tenders)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

}