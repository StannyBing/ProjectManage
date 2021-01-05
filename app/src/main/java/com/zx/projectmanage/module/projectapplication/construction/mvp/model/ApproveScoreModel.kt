package com.zx.projectmanage.module.projectapplication.construction.mvp.model


import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.construction.bean.ScoreTemplateBean

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ApproveScoreContract
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveScoreModel : BaseModel(), ApproveScoreContract.Model {
    override fun getScoreTemple(subAssessmentId: String): Observable<ScoreTemplateBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getScoreTemple(subAssessmentId)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())

    }


}