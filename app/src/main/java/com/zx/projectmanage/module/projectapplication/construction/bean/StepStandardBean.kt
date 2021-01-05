package com.zx.projectmanage.module.projectapplication.construction.bean

data class StepStandardBean(
    val comment: String,
    val createTime: String,
    val createUser: Any,
    val delFlag: String,
    val id: String,
    val name: String,
    val standardSteps: List<StepInfo>?,
    val stepCount: Int,
    val updateTime: Any,
    val updateUser: Any,
    var isSelect: Boolean = false
) {

    data class StepInfo(
        var stepId: String,
        var standardId: String,
        var stepName: String,
        var standard: String,
        var attachmentId: String,
        var sort: Int,
        var delFlag: String
    )

}