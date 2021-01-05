package com.zx.projectmanage.module.projectapplication.construction.bean

import java.io.Serializable

/**
Date:2021/1/5
Time:1:06 PM
author:qingsong
 */
data class ApproveProcessInfoBean(
    /**
     *"assessment": "string",
    "auditFlag": 0,
    "detailedProId": "string",
    "enableAssessment": 0,
    "enableResetPermissions": 0,
    "materials": "string",
    "operationGuide": "string",
    "resetPermissions": "string",
    "safetyRegulations": "string",
    "score": 0,
    "scoreFlag": 0,
    "subProcessName": "string"
     */


    var assessment: String?,
    var auditFlag: Int? = 0,
    var detailedProId: String?,
    var enableAssessment: Int? = 0,
    var enableResetPermissions: Int? = 0,
    var materials: String?,
    var operationGuide: String?,
    var resetPermissions: String?,
    var safetyRegulations: String?,
    var score: Int? = 0,
    var scoreFlag: Int? = -1,
    var subProcessName: String?
) : Serializable