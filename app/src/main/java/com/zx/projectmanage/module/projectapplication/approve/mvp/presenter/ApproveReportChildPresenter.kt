package com.zx.projectmanage.module.projectapplication.approve.mvp.presenter


import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.gt.giscollect.base.NormalList

import com.zx.projectmanage.module.projectapplication.approve.bean.ReportSubListBean
import com.zx.projectmanage.module.projectapplication.approve.mvp.contract.ApproveReportChildContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveReportChildPresenter : ApproveReportChildContract.Presenter() {


    override fun getPageSubProject(map: Map<String, String>) {
        mModel.getPageSubProject(map)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<NormalList<ReportSubListBean>>(mView) {
                override fun _onNext(t: NormalList<ReportSubListBean>?) {
                   mView.getDataSubResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, "请求失败，请检查网络后再试")
                    mView.getDataSubResult(null)

                }
            })
    }
}

