package com.zx.projectmanage.module.main.bean

import java.io.Serializable

data class UserBean(
    var code: String? = null,
    var msg: String? = null,
    var access_token: String = "",
    var user_info: Info? = null
) : Serializable {

    data class Info(
        var password: String = "",
        var username: String = "",
        var authorities: List<AuthBean> = arrayListOf(),
        var accountNonExpired: Boolean = true,
        var accountNonLocked: Boolean = true,
        var credentialsNonExpired: Boolean = true,
        var enabled: Boolean = true,
        var id: String = "",
        var deptId: String = "",
        var orgId: String = "",
        var phone: String = "",
        var avatar: String = ""
    ) : Serializable {
        data class AuthBean(var authority: String) : Serializable
    }

}