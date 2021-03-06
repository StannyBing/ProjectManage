package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import android.app.Activity
import com.allen.library.SuperTextView
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionReportChildActivity
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class ReportListAdapter(dataList: List<ReportListBean.RecordsBean>, type: Int) : ZXQuickAdapter<ReportListBean.RecordsBean, ZXBaseHolder>(R.layout.item_report_list, dataList) {

    private var type = 0

    init {
        this.type = type
    }

    override fun convert(helper: ZXBaseHolder, item: ReportListBean.RecordsBean?) {
        val superTextView = helper.getView<SuperTextView>(R.id.super_tv)
        superTextView.setLeftTopString("项目名称：${item?.projectName}")
            .setLeftBottomString("项目状态：${item?.statusDesc}")


        if (type == 0) {
            superTextView.setRightString("上报")
            if (item?.desc == "已竣工") {
                superTextView.setRightString("详情")
            }
        } else {
            superTextView.setRightString("审批")

        }



    }
}