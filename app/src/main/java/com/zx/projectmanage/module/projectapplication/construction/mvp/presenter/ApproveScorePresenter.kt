package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.construction.bean.ScoreTemplateBean
import com.zx.projectmanage.module.projectapplication.construction.dto.PostAuditDto
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ApproveScoreContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveScorePresenter : ApproveScoreContract.Presenter() {
    override fun getScoreTemple(subAssessmentId: String) {
        mModel.getScoreTemple(subAssessmentId)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<MutableList<ScoreTemplateBean>>(mView) {
                override fun _onNext(t: MutableList<ScoreTemplateBean>?) {
                    mView.onScoreTempleResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    override fun postAuditProcess(body: PostAuditDto) {
        mModel.postAuditProcess(body)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
                    mView.auditProcessResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
//                    mView.auditProcessResult(null)

                }
            })
    }


}