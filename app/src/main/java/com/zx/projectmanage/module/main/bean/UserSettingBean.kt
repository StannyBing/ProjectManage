package com.zx.projectmanage.module.main.bean

import androidx.annotation.DrawableRes

data class UserSettingBean(var name: String, @DrawableRes var icon: Int = 0, var showMargin: Boolean = false) {
}