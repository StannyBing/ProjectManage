package com.zx.projectmanage.module.main.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.main.bean.VersionBean

import com.zx.projectmanage.module.main.mvp.contract.UserContract
import okhttp3.RequestBody
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class UserModel : BaseModel(), UserContract.Model {

    override fun versionData(): Observable<VersionBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getVersion()
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }
}