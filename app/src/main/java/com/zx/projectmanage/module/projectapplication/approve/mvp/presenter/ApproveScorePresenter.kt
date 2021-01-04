package com.zx.projectmanage.module.projectapplication.approve.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.approve.bean.ScoreTemplateBean
import com.zx.projectmanage.module.projectapplication.approve.mvp.contract.ApproveScoreContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveScorePresenter : ApproveScoreContract.Presenter() {
    override fun getScoreTemple(subAssessmentId: String) {
        mModel.getScoreTemple(subAssessmentId)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<ScoreTemplateBean>(mView) {
                override fun _onNext(t: ScoreTemplateBean?) {
                    mView.onScoreTempleResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, "请求失败，请检查网络后再试")
                    mView.onScoreTempleResult(null)

                }
            })
    }


}