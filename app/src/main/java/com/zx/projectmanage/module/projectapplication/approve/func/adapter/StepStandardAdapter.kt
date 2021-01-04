package com.zx.projectmanage.module.projectapplication.approve.func.adapter

import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.approve.bean.StepStandardBean
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class StepStandardAdapter(dataList: List<StepStandardBean>) : ZXQuickAdapter<StepStandardBean, ZXBaseHolder>(R.layout.item_standard_list, dataList) {
    override fun convert(helper: ZXBaseHolder, item: StepStandardBean) {
        helper.setText(R.id.tv_standard_list_name, item.name)
        helper.setBackgroundRes(
            R.id.iv_standard_list_select, if (!item.isSelect) {
                R.mipmap.not_select
            } else {
                R.mipmap.select
            }
        )
    }
}