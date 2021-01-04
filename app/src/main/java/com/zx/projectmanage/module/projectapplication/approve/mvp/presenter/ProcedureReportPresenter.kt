package com.zx.projectmanage.module.projectapplication.approve.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.approve.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.approve.mvp.contract.ProcedureReportContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ProcedureReportPresenter : ProcedureReportContract.Presenter() {
    override fun getDeviceList(map: Map<String, String>) {
        mModel.getDeviceList(map)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<MutableList<DeviceListBean>>(mView) {
                override fun _onNext(t: MutableList<DeviceListBean>?) {
                    mView.getDeviceListResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, "请求失败，请检查网络后再试")
                    mView.getDeviceListResult(null)

                }
            })
    }


}