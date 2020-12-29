package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import com.zx.projectmanage.R

import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder

import com.zx.zxutils.other.ZXRecyclerAdapter.ZXRecyclerQuickAdapter

class ReportListAdapter(dataList: List<ReportListBean>) : ZXRecyclerQuickAdapter<ReportListBean, ZXBaseHolder>(R.layout.item_user_setting, dataList) {
    override fun quickConvert(helper: ZXBaseHolder?, item: ReportListBean?) {
        helper?.setText(R.id.tv_user_setting_name, item?.id.toString())
    }
}