package com.zx.projectmanage.module.projectapplication.construction.dto

/**
 * Date:2021/1/5
 * Time:5:47 PM
 * author:qingsong
 */
class PostAuditDto {
    /**
     * projectId : string
     * projectProcessScores : [{"createTime":"2021-01-05T09:36:52.863Z","createUser":"string","detailedProId":"string","id":"string","remarks":"string","score":0,"subAssessmentId":"string"}]
     * reportEquipmentVos : [{"auditStatus":"string","auditStatusDesc":"string","detailedProId":"string","equipmentName":"string","standardId":"string","standardProId":"string"}]
     * subProjectId : string
     */
    var projectId: String? = null
    var subProjectId: String? = null
    var projectProcessScores: List<ProjectProcessScoresBean>? = null
    var reportEquipmentVos: List<ReportEquipmentVosBean>? = null

    class ProjectProcessScoresBean {
        /**
         * createTime : 2021-01-05T09:36:52.863Z
         * createUser : string
         * detailedProId : string
         * id : string
         * remarks : string
         * score : 0
         * subAssessmentId : string
         */
        var createTime: String? = null
        var createUser: String? = null
        var detailedProId: String? = null
        var id: String? = null
        var remarks: String? = null
        var score = 0
        var subAssessmentId: String? = null

    }

    class ReportEquipmentVosBean {
        /**
         * auditStatus : string
         * auditStatusDesc : string
         * detailedProId : string
         * equipmentName : string
         * standardId : string
         * standardProId : string
         */
        var auditStatus: String? = null
        var auditStatusDesc: String? = null
        var detailedProId: String? = null
        var equipmentName: String? = null
        var standardId: String? = null
        var standardProId: String? = null

    }
}