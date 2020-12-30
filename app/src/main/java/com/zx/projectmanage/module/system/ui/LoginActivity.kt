package com.zx.projectmanage.module.system.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zx.projectmanage.R
import com.zx.projectmanage.app.toJson
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.base.UserManager
import com.zx.projectmanage.module.main.bean.UserBean
import com.zx.projectmanage.module.main.ui.MainActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionDataActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionReportActivity

import com.zx.projectmanage.module.system.mvp.contract.LoginContract
import com.zx.projectmanage.module.system.mvp.contract.SplashContract
import com.zx.projectmanage.module.system.mvp.model.LoginModel
import com.zx.projectmanage.module.system.mvp.model.SplashModel
import com.zx.projectmanage.module.system.mvp.presenter.LoginPresenter
import com.zx.projectmanage.module.system.mvp.presenter.SplashPresenter
import com.zx.zxutils.views.ZXStatusBarCompat
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Create By admin On 2017/7/11
 * 功能：登录
 */
class LoginActivity : BaseActivity<SplashPresenter, SplashModel>(), SplashContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_login
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
        et_login_username.setText(UserManager.userName)
        et_login_password.setText("")
        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        btn_login_submit.setOnClickListener {
            ConstructionDataActivity.startAction(this, false)
            if (et_login_username.text.toString().isEmpty() || et_login_password.text.toString().isEmpty()) {
                showToast("请输入用户名及密码！")
            } else {
                mPresenter.doAppLogin(
                    hashMapOf(
                        "username" to et_login_username.text.toString(),
                        "password" to et_login_password.text.toString(),
                        "grant_type" to "password"
                    ).toJson()
                )
            }
        }
    }

    override fun onAppLoginResult(userBean: UserBean?) {
        if (userBean != null) {
            showToast("登录成功")
            UserManager.userName = et_login_username.text.toString()
            UserManager.passWord = et_login_password.text.toString()
            MainActivity.startAction(this, true)
        } else {
            et_login_password.setText("")
        }
    }

}
