package com.zx.projectmanage.module.system.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zx.projectmanage.R
import com.zx.projectmanage.app.toJson
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.base.UserManager
import com.zx.projectmanage.module.main.bean.UserBean
import com.zx.projectmanage.module.main.ui.MainActivity

import com.zx.projectmanage.module.system.mvp.contract.SplashContract
import com.zx.projectmanage.module.system.mvp.model.SplashModel
import com.zx.projectmanage.module.system.mvp.presenter.SplashPresenter
import com.zx.zxutils.views.ZXStatusBarCompat


/**
 * Create By admin On 2017/7/11
 * 功能：欢迎页面
 */
class SplashActivity : BaseActivity<SplashPresenter, SplashModel>(), SplashContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, SplashActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZXStatusBarCompat.translucent(this)
        ZXStatusBarCompat.setStatusBarDarkMode(this)
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }

    override fun onResume() {
        super.onResume()
        if (UserManager.userName.isEmpty() || UserManager.passWord.isEmpty()) {
            LoginActivity.startAction(this, true)
        } else {
            //自动登录
            mPresenter.doAppLogin(
                hashMapOf(
                    "username" to UserManager.userName,
                    "password" to UserManager.passWord,
                    "grant_type" to "password"
                ).toJson()
            )
        }
    }

    override fun onAppLoginResult(userBean: UserBean?) {
        MainActivity.startAction(this, true)
        if (userBean != null) {
            showToast("登录成功")
            MainActivity.startAction(this, true)
        } else {
            LoginActivity.startAction(this, true)
        }
    }

}
