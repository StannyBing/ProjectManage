package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.construction.bean.ApproveProcessInfoBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectProcessInfoBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ApproveProcessContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveProcessPresenter : ApproveProcessContract.Presenter() {
    override fun getProcessInfo(projectId: String, subProjectId: String) {
        mModel.getProcessInfo(projectId, subProjectId)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<MutableList<ApproveProcessInfoBean>>(mView) {
                override fun _onNext(t: MutableList<ApproveProcessInfoBean>?) {
                    mView.getDataProcessResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)


                }
            })
    }


}