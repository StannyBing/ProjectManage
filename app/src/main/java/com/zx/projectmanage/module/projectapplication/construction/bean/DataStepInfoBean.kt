package com.zx.projectmanage.module.projectapplication.construction.bean

data class DataStepInfoBean(
    var path: String = "",
    var thumbnail: String = "",
    var type: Type = Type.PICTURE
) {

    enum class Type {
        PICTURE,
        VIDEO
    }

}