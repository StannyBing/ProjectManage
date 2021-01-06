package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter


import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.base.NormalList

import com.zx.projectmanage.module.projectapplication.construction.bean.ReportSubListBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportChildContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ConstructionReportChildPresenter : ConstructionReportChildContract.Presenter() {


    override fun getPageSubProject(map: Map<String, String>, type: Int) {
        mModel.getPageSubProject(map,type)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<NormalList<ReportSubListBean>>(mView) {
                override fun _onNext(t: NormalList<ReportSubListBean>?) {
                   mView.getDataSubResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)

                }
            })
    }
}

