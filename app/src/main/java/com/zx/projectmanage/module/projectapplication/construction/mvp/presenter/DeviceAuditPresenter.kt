package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.DeviceAuditContract
import okhttp3.RequestBody


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class DeviceAuditPresenter : DeviceAuditContract.Presenter() {
    override fun doReject(body: RequestBody) {
        mModel.rejectData(body)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView){
                override fun _onNext(t: Any?) {
                    mView.onRejectResult()
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }

    override fun doPass(body: RequestBody) {
        mModel.passData(body)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView){
                override fun _onNext(t: Any?) {
                    mView.onPassResult()
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }
            })
    }


}