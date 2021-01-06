package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.construction.bean.InformationBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProjectBaseInfomationContract


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
                    mView.handleError(code, message)
                }
            })
    }


}