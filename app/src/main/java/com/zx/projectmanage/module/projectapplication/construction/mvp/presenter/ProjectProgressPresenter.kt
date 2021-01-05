package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.construction.bean.ProcessProgressBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProjectProgressContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ProjectProgressPresenter : ProjectProgressContract.Presenter() {
    override fun getProcessProgress(detailedProId: String) {

        mModel.getProcessProgress(detailedProId)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<MutableList<ProcessProgressBean>>(mView) {
                override fun _onNext(t: MutableList<ProcessProgressBean>?) {
                    mView.getProgressResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, "请求失败，请检查网络后再试")
                    mView.getProgressResult(null)

                }
            })
    }


}