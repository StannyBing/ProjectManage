package com.zx.projectmanage.module.projectapplication.construction.mvp.model


import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectPeriodBean


import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportContract

import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ConstructionReportModel : BaseModel(), ConstructionReportContract.Model {
    /**
     * 获取项目列表
     */
    override fun getPageProject(
        districtCode: String?,
        keyword: String?,
        pageNo: Int?,
        pageSize: Int?,
        projectStatus: String?,
        tenders: Int?,
        buildPeriod: String?,
        type: Int
    ): Observable<ReportListBean> {
        if (type == 0) {
            return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
                .getPageProject(districtCode, keyword, pageNo, pageSize, projectStatus, tenders, buildPeriod)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main())
        } else {
            return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
                .getApproveProject(districtCode, keyword, pageNo, pageSize, projectStatus, tenders, buildPeriod)
                .compose(RxHelper.handleResult())
                .compose(RxSchedulers.io_main())
        }

    }

    /**
     * 获取所有工序状态
     */
    override fun getProjectStatus(): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getProjectStatus()
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())

    }

    /**
     * 获取所有工序状态
     */
    override fun getProjectPeriod(): Observable<MutableList<ProjectPeriodBean>> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getProjectPeriod()
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }
}