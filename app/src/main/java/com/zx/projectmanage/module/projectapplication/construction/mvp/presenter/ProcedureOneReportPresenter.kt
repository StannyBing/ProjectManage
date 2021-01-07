package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProcedureOneReportContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProcedureReportContract
import okhttp3.RequestBody


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ProcedureOneReportPresenter : ProcedureOneReportContract.Presenter() {
    override fun getDeviceList(map: Map<String, String>) {
        mModel.getDeviceList(map)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<MutableList<DeviceListBean>>(mView) {
                override fun _onNext(t: MutableList<DeviceListBean>?) {
                    mView.getDeviceListResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)

                }
            })
    }

    override fun postSubmit(body: RequestBody) {
        mModel.postSubmit(body)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
                    mView.postSubmitResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
//                    mView.postSubmitResult(null)
                }
            })
    }
}
