package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import android.app.Activity
import androidx.annotation.Nullable
import com.allen.library.SuperTextView
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportSubListBean
import com.zx.projectmanage.module.projectapplication.construction.ui.MacroReportInfoActivity
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class ReportChildListAdapter(@Nullable data: List<ReportSubListBean>?, type: Int) : ZXQuickAdapter<ReportSubListBean?, ZXBaseHolder?>(R.layout.item_report_list, data) {

    private var type=0
    init {
        this.type= type
    }

    override fun convert(helper: ZXBaseHolder, item: ReportSubListBean?) {
        val superTextView = helper?.getView<SuperTextView>(R.id.super_tv)
        superTextView.setLeftTopString("项目名称：${item?.subProjectName}")
            .setLeftBottomString("项目位置：${item?.address}")
            .setRightString(item?.statusDesc)
    }
}