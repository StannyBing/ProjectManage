package com.zx.projectmanage.module.projectapplication.construction.bean

import android.graphics.PointF
import com.zx.zxutils.other.QuickAdapter.entity.MultiItemEntity

data class ConstructionDataBean(
    var type: Int,
    var name: String,
    var stringValue: String = "",
    var longitude: Double? = null,
    var latitude: Double? = null,
    var stepInfos: ArrayList<DataStepInfoBean> = arrayListOf(),
    var isDivider: Boolean = false,
    var standardBean: StepStandardBean? = null
) : MultiItemEntity {

    companion object {
        const val Title_Type = -1
        const val Edit_Type = 0
        const val Text_Type = 1
        const val Select_Type = 2
        const val Location_Type = 3
        const val Step_Type = 4
    }

    override fun getItemType(): Int {
        return type
    }

}
