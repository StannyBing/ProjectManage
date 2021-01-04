package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import android.app.Activity
import androidx.annotation.Nullable
import com.allen.library.SuperTextView
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.approve.bean.ReportSubListBean
import com.zx.projectmanage.module.projectapplication.approve.ui.MacroApproveInfoActivity
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class ReportChildListAdapter(@Nullable data: List<ReportSubListBean>?) : ZXQuickAdapter<ReportSubListBean?, ZXBaseHolder?>(R.layout.item_report_list, data) {


    override fun convert(helper: ZXBaseHolder, item: ReportSubListBean?) {
        val superTextView = helper?.getView<SuperTextView>(R.id.super_tv)
        superTextView.setLeftTopString("项目名称：${item?.getSubProjectName()}")
            .setLeftBottomString("项目位置：${item?.getAddress()}")
            .setRightString(item?.getStatusDesc())

        superTextView.setOnSuperTextViewClickListener {
            item?.let {
                MacroApproveInfoActivity.startAction(
                    mContext as Activity, false,
                    item?.getProcessId().toString(), item?.getProjectId().toString(), item?.getSubProjectId().toString()
                )
            }
        }
    }
}