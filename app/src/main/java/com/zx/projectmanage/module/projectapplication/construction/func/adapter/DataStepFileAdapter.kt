package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.approve.bean.DataStepInfoBean
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter
import java.io.File

class DataStepFileAdapter(dataList: List<DataStepInfoBean>) : ZXQuickAdapter<DataStepInfoBean, ZXBaseHolder>(R.layout.item_data_step_file, dataList) {
    override fun convert(helper: ZXBaseHolder, item: DataStepInfoBean) {
        Glide.with(mContext)
            .load(
                if (item.thumbnail.startsWith("http")) {
                    item.thumbnail
                } else {
                    File(item.thumbnail)
                }
            )
            .apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop(), RoundedCorners(20))))
            .into(helper.getView(R.id.iv_data_step_image))
        helper.setGone(R.id.tv_data_step_shadow, helper.adapterPosition == 0)
        helper.setGone(R.id.iv_data_step_delete, helper.adapterPosition != 0)
        helper.addOnClickListener(R.id.iv_data_step_delete)
    }
}