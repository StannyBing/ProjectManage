package com.zx.projectmanage.module.main.func.adapter

import com.zx.projectmanage.R
import com.zx.projectmanage.module.main.bean.UserSettingBean
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class UserSettingAdapter(dataList: List<UserSettingBean>) : ZXQuickAdapter<UserSettingBean, ZXBaseHolder>(R.layout.item_user_setting, dataList) {
    override fun convert(helper: ZXBaseHolder, item: UserSettingBean) {
        helper.setText(R.id.tv_user_setting_name, item.name)
        helper.setGone(R.id.view_user_setting_divider, item.showMargin)
    }
}