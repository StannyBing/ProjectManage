package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import android.app.Activity
import com.allen.library.SuperTextView
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.approve.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.approve.ui.ApproveReportChildActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionReportActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionReportChildActivity
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class ReportListAdapter(dataList: List<ReportListBean.RecordsBean>) : ZXQuickAdapter<ReportListBean.RecordsBean, ZXBaseHolder>(R.layout.item_report_list, dataList) {
    override fun convert(helper: ZXBaseHolder, item: ReportListBean.RecordsBean?) {
        val superTextView = helper?.getView<SuperTextView>(R.id.super_tv)
        superTextView.setLeftTopString("项目名称：${item?.projectName}")
            .setLeftBottomString("项目状态：${item?.projectDesc}")
            .setRightString("上报")

        if (item?.projectDesc == "已竣工") {
            superTextView.setRightString("详情")
        }

        superTextView.setOnSuperTextViewClickListener {
            item?.let {
                ConstructionReportChildActivity.startAction(mContext as Activity, false, it.projectId.toString(), it.projectName.toString())
            }
        }

    }
}