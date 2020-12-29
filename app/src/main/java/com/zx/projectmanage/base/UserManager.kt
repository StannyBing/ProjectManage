package com.zx.projectmanage.base

import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.app.MyApplication
import com.zx.projectmanage.module.main.bean.UserBean
import com.zx.zxutils.util.ZXSharedPrefUtil

/**
 * Created by Xiangb on 2019/3/5.
 * 功能：用户管理器
 */
object UserManager {

    private var user: UserBean? = null

    var userName: String = ""
        set(value) {
            val sharedPref = MyApplication.mSharedPrefUtil
            sharedPref.putString("m_username", value)
            field = value
        }
        get() {
            if (field.isEmpty()) {
                val sharedPref = MyApplication.mSharedPrefUtil
                return sharedPref.getString("m_username")
            } else {
                return field
            }
        }

    var passWord: String = ""
        set(value) {
            val sharedPref = MyApplication.mSharedPrefUtil
            sharedPref.putString("m_password", value)
            field = value
        }
        get() {
            if (field.isEmpty()) {
                val sharedPref = MyApplication.mSharedPrefUtil
                return sharedPref.getString("m_password")
            } else {
                return field
            }
        }

    fun getUser(): UserBean {
        if (user == null) {
            val sharedPref = MyApplication.mSharedPrefUtil
            user = sharedPref.getObject("userBean")
            if (user == null) {
                user = UserBean("")
            }
        }
        return user!!
    }

    fun setUser(userBean: UserBean) {
        ApiConfigModule.COOKIE = userBean.access_token
        user = userBean
        saveUser()
    }

    fun saveUser() {
        val sharedPref = MyApplication.mSharedPrefUtil
        sharedPref.putObject("userBean", user)
    }

    fun loginOut() {
        ApiConfigModule.COOKIE = ""
        passWord = ""
        saveUser()
    }

}