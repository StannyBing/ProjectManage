package com.zx.projectmanage.module.projectapplication.construction.bean

import java.io.Serializable

/**
Date:2020/12/29
Time:3:49 PM
author:qingsong
 */

class ProjectProcessInfoBean() : Serializable {

    /**
     * processId : 1341276250770563074
     * processName : 测试修复
     * remarks : fsdafs
     * createTime : 2020-12-30 11:32:38
     * createUser : null
     * updateTime : 2020-12-30 11:32:38
     * updateUser : null
     * delFlag : null
     * detailedList : [{"id":"1344208677254467585","processId":"1341276250770563074","approvalMode":"354724257168625664","assessment":"354136375345287168,354134317070290944,356239445776666624,1","operationGuide":"356633892335128576","safetyRegulations":"356633901403213824","materials":"356633910802649088","subProcessName":"工序1","scoreRights":"354136375345287168,354134317070290944,356239445776666624","resetPermissions":"","enableAssessment":0,"enableScoreRights":1,"enableResetPermissions":0,"sort":0,"showOperationGuide":1,"showSafetyRegulations":1,"showMaterials":1,"createTime":"2020-12-31 12:40:19","createUser":null,"updateTime":"2020-12-31 04:40:23","updateUser":null,"participants":"354136375345287168,354134317070290944,356239445776666624,1,354344298709258240","delFlag":"0"},{"id":"1344209269234339841","processId":"1341276250770563074","approvalMode":"354724257168625664","assessment":"354136375345287168,354134317070290944,356239445776666624","operationGuide":"356634417109667840","safetyRegulations":"356634425183703040","materials":"356634434788659200","subProcessName":"工序1","scoreRights":"354136375345287168,354134317070290944","resetPermissions":"","enableAssessment":0,"enableScoreRights":1,"enableResetPermissions":0,"sort":0,"showOperationGuide":1,"showSafetyRegulations":1,"showMaterials":1,"createTime":"2020-12-31 12:40:23","createUser":null,"updateTime":"2020-12-31 04:40:26","updateUser":null,"participants":"354136375345287168,354134317070290944,356239445776666624","delFlag":"0"}]
     * processCount : null
     */
    var processId: String? = null
    var processName: String? = null
    var remarks: String? = null
    var createTime: String? = null
    var createUser: Any? = null
    var updateTime: String? = null
    var updateUser: Any? = null
    var delFlag: Any? = null
    var processCount: Any? = null
    var detailedList: List<DetailedListBean?>? = null


    class DetailedListBean() : Serializable {
        /**
         * id : 1344208677254467585
         * processId : 1341276250770563074
         * approvalMode : 354724257168625664
         * assessment : 354136375345287168,354134317070290944,356239445776666624,1
         * operationGuide : 356633892335128576
         * safetyRegulations : 356633901403213824
         * materials : 356633910802649088
         * subProcessName : 工序1
         * scoreRights : 354136375345287168,354134317070290944,356239445776666624
         * resetPermissions :
         * enableAssessment : 0
         * enableScoreRights : 1
         * enableResetPermissions : 0
         * sort : 0
         * showOperationGuide : 1
         * showSafetyRegulations : 1
         * showMaterials : 1
         * createTime : 2020-12-31 12:40:19
         * createUser : null
         * updateTime : 2020-12-31 04:40:23
         * updateUser : null
         * participants : 354136375345287168,354134317070290944,356239445776666624,1,354344298709258240
         * delFlag : 0
         */
        var id: String? = null
        var processId: String? = null
        var approvalMode: String? = null
        var assessment: String? = null
        var operationGuide: String? = null
        var safetyRegulations: String? = null
        var materials: String? = null
        var subProcessName: String? = null
        var scoreRights: String? = null
        var resetPermissions: String? = null
        var enableAssessment = 0
        var enableScoreRights = 0
        var enableResetPermissions = 0
        var sort = 0
        var showOperationGuide = 0
        var showSafetyRegulations = 0
        var showMaterials = 0
        var createTime: String? = null
        var createUser: Any? = null
        var updateTime: String? = null
        var updateUser: Any? = null
        var participants: String? = null
        var delFlag: String? = null
        var auditStatus: String? = null
        var score: Double? = null
        var processDetailedProId: String? = null
        var processType: String? = null
    }

}