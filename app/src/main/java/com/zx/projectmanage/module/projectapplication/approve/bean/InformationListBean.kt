package com.zx.projectmanage.module.projectapplication.approve.bean

import com.zx.zxutils.other.QuickAdapter.entity.MultiItemEntity

/**
Date:2020/12/30
Time:7:21 PM
author:qingsong
 */
data class InformationListBean(var titel: String, val text: String? = null, var type: Int) : MultiItemEntity {

    companion object {
        const val Title_Type = 1
        const val Text_Type = 2

    }

    override fun getItemType(): Int {
        return type
    }
}
