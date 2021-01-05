package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.construction.bean.InformationListBean
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXMultiItemQuickAdapter


class BaseInfomationAdapter(dataList: List<InformationListBean>) : ZXMultiItemQuickAdapter<InformationListBean, ZXBaseHolder>(dataList) {
    init {
        addItemType(InformationListBean.Title_Type, R.layout.item_information)
        addItemType(InformationListBean.Text_Type, R.layout.item_information_text)
    }

    override fun convert(helper: ZXBaseHolder, item: InformationListBean) {

        when (item.type) {
            InformationListBean.Title_Type -> {
                helper.setText(R.id.title, item.titel)
            }
            InformationListBean.Text_Type -> {
                helper.setText(R.id.title, item.titel)
                helper.setText(R.id.text, item.text)
            }
        }
    }
}