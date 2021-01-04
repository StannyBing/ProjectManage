package com.zx.projectmanage.module.projectapplication.approve.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.approve.bean.ProjectProcessInfoBean
import com.zx.projectmanage.module.projectapplication.approve.mvp.contract.MacroReportInfoContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class MacroReportInfoPresenter : MacroReportInfoContract.Presenter() {

    override fun getProcessInfo(id: String) {
        mModel.getProcessInfo(id)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<ProjectProcessInfoBean>(mView) {
                override fun _onNext(t: ProjectProcessInfoBean?) {
                    mView.getDataProcessResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, "请求失败，请检查网络后再试")
                    mView.getDataProcessResult(null)

                }
            })
    }


}