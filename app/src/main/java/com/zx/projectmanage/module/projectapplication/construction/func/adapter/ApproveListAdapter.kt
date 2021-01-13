package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import com.allen.library.SuperTextView
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class ApproveListAdapter(dataList: MutableList<DeviceListBean>) : ZXQuickAdapter<DeviceListBean, ZXBaseHolder>(R.layout.item_report_list, dataList) {
    override fun convert(helper: ZXBaseHolder, item: DeviceListBean?) {
        val superTextView = helper.getView<SuperTextView>(R.id.super_tv)
        superTextView.setLeftString(item?.gaugingPoint)
        if (item?.status == "2" || item?.status == "9") {
            superTextView.setRightString("${item?.auditStatusDesc}(${item?.fraction})")
        } else {
            superTextView.setRightString(item?.auditStatusDesc)
        }

    }
}