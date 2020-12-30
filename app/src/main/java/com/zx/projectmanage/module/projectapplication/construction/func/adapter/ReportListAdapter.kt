package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import android.app.Activity
import android.os.Bundle
import com.allen.library.SuperTextView
import com.allen.library.SuperTextView.BOTTOM
import com.zx.projectmanage.R

import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionReportChildActivity
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class ReportListAdapter(dataList: List<ReportListBean>) : ZXQuickAdapter<ReportListBean, ZXBaseHolder>(R.layout.item_report_list, dataList) {
    override fun convert(helper: ZXBaseHolder, item: ReportListBean?) {
        val superTextView = helper?.getView<SuperTextView>(R.id.reportList)
        superTextView.setLeftTopString("项目名称：xxxxx")
            .setLeftBottomString("项目状态：xxxxx")
            .setRightString("上报")
        superTextView.setOnClickListener {
            ConstructionReportChildActivity.startAction(mContext as Activity, false, item?.id!!, "xxx项目")
        }
    }
}