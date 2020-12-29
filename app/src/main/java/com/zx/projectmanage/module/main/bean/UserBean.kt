package com.zx.projectmanage.module.main.bean

import java.io.Serializable

data class UserBean(
    var code: String? = null,
    var msg: String? = null,
    var access_token: String = ""
) : Serializable {
}