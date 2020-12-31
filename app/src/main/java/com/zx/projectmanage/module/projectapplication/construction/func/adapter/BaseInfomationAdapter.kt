package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import android.app.Activity
import android.os.Bundle
import com.allen.library.SuperTextView
import com.allen.library.SuperTextView.BOTTOM
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.construction.bean.InformationBean

import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionReportChildActivity
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class BaseInfomationAdapter(dataList: List<InformationBean>) : ZXQuickAdapter<InformationBean, ZXBaseHolder>(R.layout.item_report_list, dataList) {
    override fun convert(helper: ZXBaseHolder, item: InformationBean) {
        val superTextView = helper?.getView<SuperTextView>(R.id.super_tv)
        if (item.type == 1) {
            superTextView.setLeftString(item.titel)
                .setLeftIcon(R.drawable.title_rangle)
        } else {
            superTextView.setLeftString(item.titel)
                .setRightString(item.text)
        }


    }
}