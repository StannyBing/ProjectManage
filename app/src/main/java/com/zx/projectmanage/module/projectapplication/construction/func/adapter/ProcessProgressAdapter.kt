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
            .setText(R.id.status, item?.auditStatus)
            .setText(R.id.name, item?.realName)
            .setText(R.id.personType, item?.postName)
    }
}