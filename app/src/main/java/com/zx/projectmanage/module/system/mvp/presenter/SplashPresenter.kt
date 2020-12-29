package com.zx.projectmanage.module.system.mvp.presenter

import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.base.UserManager
import com.zx.projectmanage.module.main.bean.UserBean
import com.zx.projectmanage.module.system.mvp.contract.SplashContract
import okhttp3.RequestBody


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class SplashPresenter : SplashContract.Presenter() {
    override fun doAppLogin(body: RequestBody) {
        ApiConfigModule.COOKIE = ""
        mModel.appLoginData(body)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<UserBean>(mView) {
                override fun _onNext(t: UserBean?) {
                    if (t != null) {
                        UserManager.setUser(t)
                    }
                    mView.onAppLoginResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, message)
                    mView.onAppLoginResult(null)
                    UserManager.loginOut()
                }
            })
    }

}