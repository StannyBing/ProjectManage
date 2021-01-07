package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import android.app.Activity
import com.allen.library.SuperTextView
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.construction.bean.ProcessProgressBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionReportChildActivity
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class ProcessProgressAdapter(dataList: List<ProcessProgressBean>) : ZXQuickAdapter<ProcessProgressBean, ZXBaseHolder>(R.layout.item_project_progress, dataList) {


    override fun convert(helper: ZXBaseHolder, item: ProcessProgressBean?) {
        helper
            .setText(R.id.name, item?.realName)
            .setText(R.id.personType, "(${item?.postName})")
            .setText(R.id.resion, if (item?.auditReason == null) "" else item.auditReason)
        when (item?.auditStatus) {
            "1" -> {
                helper
                    .setText(R.id.status, "提交资料")
            }
            "4" -> {
                helper
                    .setText(R.id.status, "审核通过")
            }
            "9" -> {
                helper
                    .setText(R.id.status, "驳回")
            }
        }
    }
}