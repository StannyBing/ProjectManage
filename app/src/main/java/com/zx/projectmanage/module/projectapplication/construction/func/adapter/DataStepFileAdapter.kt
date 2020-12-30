package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.construction.bean.DataStepInfoBean
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter

class DataStepFileAdapter(dataList: List<DataStepInfoBean>) : ZXQuickAdapter<DataStepInfoBean, ZXBaseHolder>(R.layout.item_data_step_file, dataList) {
    override fun convert(helper: ZXBaseHolder, item: DataStepInfoBean) {
        Glide.with(mContext)
            .load(item.path)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .into(helper.getView(R.id.iv_data_step_image))
        helper.addOnClickListener(R.id.iv_data_step_delete)
    }
}