package com.zx.projectmanage.module.projectapplication.approve.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.approve.bean.InformationBean
import com.zx.projectmanage.module.projectapplication.approve.mvp.contract.ProjectBaseInfomationContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ProjectBaseInfomationPresenter : ProjectBaseInfomationContract.Presenter() {
    override fun getProjectInformation(projectId: String) {
        mModel.getProjectInformation(projectId)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<InformationBean>(mView) {
                override fun _onNext(t: InformationBean?) {
                    mView.getDataInformationResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, "请求失败，请检查网络后再试")
                    mView.getDataInformationResult(null)

                }
            })
    }


}