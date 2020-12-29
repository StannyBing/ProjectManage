package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import com.allen.library.SuperTextView
import com.zx.projectmanage.R

import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder

import com.zx.zxutils.other.ZXRecyclerAdapter.ZXRecyclerQuickAdapter

class ReportListAdapter(dataList: List<ReportListBean>) : ZXRecyclerQuickAdapter<ReportListBean, ZXBaseHolder>(0, dataList) {
    override fun quickConvert(helper: ZXBaseHolder?, item: ReportListBean?) {
        val superTextView = SuperTextView(mContext)
        superTextView.setLeftTopString("项目名称：xxxxx")
            .setLeftBottomString("项目状态：xxxxx")
            .setRightString("上报")
            .setRightIcon(R.drawable.__picker_camera)
    }
}