package com.zx.projectmanage.module.projectapplication.func.adater

import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.bean.ApplicationFuncBean
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class ApplicationFuncsAdapter(dataList: List<ApplicationFuncBean>) :
    ZXQuickAdapter<ApplicationFuncBean, ZXBaseHolder>(R.layout.item_application_func, dataList) {
    override fun convert(helper: ZXBaseHolder, item: ApplicationFuncBean) {
        helper.setText(R.id.tv_func_name, item.name)
        helper.setImageResource(R.id.iv_func_icon, item.icon)
    }
}