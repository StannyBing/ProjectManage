package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectPeriodBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ConstructionReportPresenter : ConstructionReportContract.Presenter() {
    override fun getPageProject(
        districtCode: String?,
        keyword: String?,
        pageNo: Int?,
        pageSize: Int?,
        projectStatus: String?,
        tenders: Int?,
        buildPeriod: String?,
        type:Int
    ) {
        mModel.getPageProject(districtCode, keyword, pageNo, pageSize, projectStatus, tenders, buildPeriod,type)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<ReportListBean>(mView) {
                override fun _onNext(t: ReportListBean?) {
                    mView.getDataResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    override fun getProjectStatus() {
        mModel.getProjectStatus()
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
//                    mView.showToast(t.toString())
                    mView.getProjectStatusResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)

                }
            })
    }

    override fun getProjectPeriod() {
        mModel.getProjectPeriod()
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<MutableList<ProjectPeriodBean>>(mView) {
                override fun _onNext(t: MutableList<ProjectPeriodBean>?) {
                    mView.getProjectPeriodResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)

                }
            })
    }


}