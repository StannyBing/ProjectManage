package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.bean.StepStandardBean
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class Process1DeviceAdapter(dataList: List<DeviceListBean>) : ZXQuickAdapter<DeviceListBean, ZXBaseHolder>(R.layout.item_standard_list, dataList) {
    override fun convert(helper: ZXBaseHolder, item: DeviceListBean) {
        helper.setText(R.id.tv_standard_list_name, item.equipmentName)
        helper.setBackgroundRes(
            R.id.iv_standard_list_select, if (!item.isSelect) {
                R.mipmap.not_select
            } else {
                R.mipmap.select
            }
        )
    }
}