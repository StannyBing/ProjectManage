package com.zx.projectmanage.module.projectapplication.construction.mvp.model


import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.basebean.BaseRespose
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService


import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.dto.ReportListDto

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportContract

import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ConstructionReportModel : BaseModel(), ConstructionReportContract.Model {
    override fun getPageProject( districtCode: String?,
                                 keyword: String?,
                                 pageNo: Int?,
                                 pageSize: Int?,
                                 projectStatus: Int?,
                                 tenders: Int?): Observable<ReportListBean>{
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getPageProject(districtCode, keyword, pageNo, pageSize, projectStatus, tenders)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }
}