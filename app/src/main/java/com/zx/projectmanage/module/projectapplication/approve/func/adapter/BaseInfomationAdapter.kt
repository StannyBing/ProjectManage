package com.zx.projectmanage.module.projectapplication.approve.func.adapter

import com.allen.library.SuperTextView
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.approve.bean.ConstructionDataBean
import com.zx.projectmanage.module.projectapplication.approve.bean.InformationBean

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