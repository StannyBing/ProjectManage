package com.zx.projectmanage.module.projectapplication.construction.bean

import java.io.File

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