package com.zx.projectmanage.module.projectapplication.construction.mvp.contract

import com.frame.zxmvp.base.BasePresenter
import com.frame.zxmvp.base.IView
import com.frame.zxmvp.base.IModel
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.bean.StepStandardBean
import okhttp3.RequestBody
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
interface DeviceAuditContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun onRejectResult()

        fun onPassResult()

        fun onStepDetailResult(stepDetail: StepStandardBean)

        fun onDeviceDetailResult(deviceListBean: DeviceListBean)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun rejectData(body: RequestBody): Observable<Any>

        fun passData(body: RequestBody): Observable<Any>

        fun stepDetailData(id: String): Observable<StepStandardBean>

        fun deviceDetailData(standardProId: String, standardId: String): Observable<DeviceListBean>
    }

    //方法
    abstract class Presenter : BasePresenter<View, Model>() {
        abstract fun doReject(body: RequestBody)

        abstract fun doPass(body: RequestBody)

        abstract fun getStepDetail(id: String)

        abstract fun getDeviceDetail(standardProId: String, standardId: String)
    }
}

