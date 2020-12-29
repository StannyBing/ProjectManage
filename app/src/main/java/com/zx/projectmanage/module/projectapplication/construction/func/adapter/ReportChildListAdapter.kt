package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import android.app.Activity
import com.allen.library.SuperTextView
import com.zx.projectmanage.R

import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionReportChildActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.MacroReportInfoActivity
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

import com.zx.zxutils.other.ZXRecyclerAdapter.ZXRecyclerQuickAdapter

class ReportChildListAdapter(dataList: List<ReportListBean>) : ZXQuickAdapter<ReportListBean, ZXBaseHolder>(R.layout.item_report_list, dataList) {
    override fun convert(helper: ZXBaseHolder, item: ReportListBean?) {
        val superTextView = helper?.getView<SuperTextView>(R.id.reportList)
        superTextView.setLeftTopString("项目名称：xxxxx")
            .setLeftBottomString("项目位置：xxxxx")
            .setRightString("未通过")
        superTextView.setRightTextGroupClickListener {
            MacroReportInfoActivity.startAction(mContext as Activity, false)
        }
    }

}