package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import com.frame.zxmvp.basebean.BaseRespose
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportChildContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ConstructionReportChildPresenter : ConstructionReportChildContract.Presenter() {


    override fun getPageSubProject(districtCode: String?, subProjectName: String?, pageNo: Int?, pageSize: Int?, status: Int?, projectId: String, tenders: Int?) {
        ApiConfigModule.COOKIE = ""
        mModel.getPageSubProject(districtCode, subProjectName, pageNo, pageSize, status, projectId, tenders)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<ReportListBean>(mView) {
                override fun _onNext(t: ReportListBean?) {
                    mView.getDataResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, "请求失败，请检查网络后再试")
                    mView.getDataResult(null)

                }
            })
    }
}

