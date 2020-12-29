package com.zx.projectmanage.module.system.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.main.bean.UserBean

import com.zx.projectmanage.module.system.mvp.contract.SplashContract
import okhttp3.RequestBody
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class SplashModel : BaseModel(), SplashContract.Model {
    override fun appLoginData(body: RequestBody): Observable<UserBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .doAppLogin(body)
//            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

}