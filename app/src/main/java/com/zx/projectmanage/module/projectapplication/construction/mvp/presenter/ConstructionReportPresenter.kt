package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectPeriodBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectStatusBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportContract
import rx.functions.Func1


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
        projectStatus: Int?,
        tenders: Int?
    ) {
        mModel.getPageProject(districtCode, keyword, pageNo, pageSize, projectStatus, tenders)
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

    override fun getProjectStatus() {
        mModel.getProjectStatus()
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
                    mView.getProjectStatusResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, "请求失败，请检查网络后再试")
                    mView.getProjectStatusResult(null)

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
                    mView.handleError(code, "请求失败，请检查网络后再试")
                    mView.getProjectPeriodResult(null)

                }
            })
    }


}