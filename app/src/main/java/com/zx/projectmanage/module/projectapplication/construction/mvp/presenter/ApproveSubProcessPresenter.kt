package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.dto.PostAuditDto
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ApproveSubProcessContract
import okhttp3.RequestBody


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveSubProcessPresenter : ApproveSubProcessContract.Presenter() {
    override fun getDeviceList(detailedProId: String) {
        mModel.getDeviceList(detailedProId)
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

    override fun postAuditProcess(body: PostAuditDto) {
        mModel.postAuditProcess(body)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<Any>(mView) {
                override fun _onNext(t: Any?) {
                    mView.auditProcessResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, "请求失败，请检查网络后再试")
                    mView.auditProcessResult(null)

                }
            })
    }


}