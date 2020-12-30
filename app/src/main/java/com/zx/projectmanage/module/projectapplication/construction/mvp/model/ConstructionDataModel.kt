package com.zx.projectmanage.module.projectapplication.construction.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.construction.bean.BaiduGeocoderBean

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionDataContract
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ConstructionDataModel : BaseModel(), ConstructionDataContract.Model {
    override fun baiduGeocoderData(url: String): Observable<BaiduGeocoderBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .geocoder(url)
            .compose(RxSchedulers.io_main())
    }


}