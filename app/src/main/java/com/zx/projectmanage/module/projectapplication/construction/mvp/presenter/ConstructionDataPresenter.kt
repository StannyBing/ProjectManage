package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import android.graphics.PointF
import android.location.Location
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.module.projectapplication.construction.bean.BaiduGeocoderBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionDataContract


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ConstructionDataPresenter : ConstructionDataContract.Presenter() {
    override fun doGeocoder(location: Location) {
        val url = "https://apis.map.qq.com/ws/geocoder/v1/?location=${location.latitude},${location.longitude}&key=UYZBZ-P4RE4-WWBUC-XJ3JG-QJPST-IPB26"
        mModel.baiduGeocoderData(url)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<BaiduGeocoderBean>(mView) {
                override fun _onNext(t: BaiduGeocoderBean?) {
                    if (t != null) {
                        mView.onGeocoderResult(location, t)
                    }
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                }

            })
    }


}