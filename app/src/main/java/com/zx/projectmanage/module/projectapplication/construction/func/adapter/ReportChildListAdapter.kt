package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import android.app.Activity
import androidx.annotation.Nullable
import com.allen.library.SuperTextView
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportSubListBean
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionReportChildActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.MacroReportInfoActivity
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter
import com.zx.zxutils.other.ZXRecyclerAdapter.ZXRecyclerQuickAdapter

class ReportChildListAdapter(@Nullable data: List<ReportSubListBean>?) : ZXQuickAdapter<ReportSubListBean?, ZXBaseHolder?>(R.layout.item_report_list, data) {


    override fun convert(helper: ZXBaseHolder, item: ReportSubListBean?) {
        val superTextView = helper?.getView<SuperTextView>(R.id.super_tv)
        superTextView.setLeftTopString("项目名称：${item?.getSubProjectName()}")
            .setLeftBottomString("项目位置：${item?.getAddress()}")
            .setRightString(item?.getStatusDesc())
        superTextView.setOnSuperTextViewClickListener {
            item?.let {
                MacroReportInfoActivity.startAction(mContext as Activity, false, item?.getProcessId().toString())
            }
        }
    }
}