package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectProcessInfoBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.MacroReportInfoContract

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class MacroReportInfoPresenter : MacroReportInfoContract.Presenter() {

    override fun getProcessInfo(subProjectId: String, id: String) {
        mModel.getProcessInfo(subProjectId, id)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<ProjectProcessInfoBean>(mView) {
                override fun _onNext(t: ProjectProcessInfoBean?) {
                    mView.getDataProcessResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)

                }
            })
    }


}