package com.zx.projectmanage.module.projectapplication.construction.bean

/**
Date:2020/12/29
Time:3:49 PM
author:qingsong
 */

class ReportListBean {

    /**
     * current : 0
     * hitCount : true
     * optimizeCountSql : true
     * orders : [{"asc":true,"column":"string"}]
     * pages : 0
     * records : [{"assessmentId":"string","buildPeriod":"string","completedTime":"2020-12-30T05:50:26.687Z","createTime":"2020-12-30T05:50:26.687Z","createUser":"string","delFlag":"string","districtCode":"string","finalJudgmen":"string","latitude":"string","longitude":"string","mainProjectName":"string","participates":[{"approvalOrder":0,"chargeUser":"string","contactWay":"string","createTime":"2020-12-30T05:50:26.687Z","createUser":"string","delFlag":"string","id":"string","orgId":"string","orgName":"string","projectId":"string","superviseUser":"string","type":"string","updateTime":"2020-12-30T05:50:26.687Z","updateUser":"string"}],"projectAddress":"string","projectDesc":"string","projectId":"string","projectMeasures":"string","projectName":"string","projectNumber":"string","projectStatus":0,"projectSurvey":"string","recordNo":"string","remarks":"string","score":0,"subProjectCount":0,"tenders":"string","updateTime":"2020-12-30T05:50:26.687Z","updateUser":"string"}]
     * searchCount : true
     * size : 0
     * total : 0
     */
   var current = 0
   var hitCount = false
   var optimizeCountSql = false
   var pages = 0
   var searchCount = false
   var size = 0
   var total = 0
   var orders: List<OrdersBean?>? = null
   var records: List<RecordsBean?>? = null

    class OrdersBean {
        /**
         * asc : true
         * column : string
         */
        var isAsc = false
        var column: String? = null

    }

    class RecordsBean {
        /**
         * assessmentId : string
         * buildPeriod : string
         * completedTime : 2020-12-30T05:50:26.687Z
         * createTime : 2020-12-30T05:50:26.687Z
         * createUser : string
         * delFlag : string
         * districtCode : string
         * finalJudgmen : string
         * latitude : string
         * longitude : string
         * mainProjectName : string
         * participates : [{"approvalOrder":0,"chargeUser":"string","contactWay":"string","createTime":"2020-12-30T05:50:26.687Z","createUser":"string","delFlag":"string","id":"string","orgId":"string","orgName":"string","projectId":"string","superviseUser":"string","type":"string","updateTime":"2020-12-30T05:50:26.687Z","updateUser":"string"}]
         * projectAddress : string
         * projectDesc : string
         * projectId : string
         * projectMeasures : string
         * projectName : string
         * projectNumber : string
         * projectStatus : 0
         * projectSurvey : string
         * recordNo : string
         * remarks : string
         * score : 0
         * subProjectCount : 0
         * tenders : string
         * updateTime : 2020-12-30T05:50:26.687Z
         * updateUser : string
         */
        var assessmentId: String? = null
        var buildPeriod: String? = null
        var completedTime: String? = null
        var createTime: String? = null
        var createUser: String? = null
        var delFlag: String? = null
        var districtCode: String? = null
        var finalJudgmen: String? = null
        var latitude: String? = null
        var longitude: String? = null
        var mainProjectName: String? = null
        var projectAddress: String? = null
        var projectDesc: String? = null
        var projectId: String? = null
        var projectMeasures: String? = null
        var projectName: String? = null
        var projectNumber: String? = null
        var projectStatus = 0
        var projectSurvey: String? = null
        var recordNo: String? = null
        var remarks: String? = null
        var score = 0
        var subProjectCount = 0
        var tenders: String? = null
        var updateTime: String? = null
        var updateUser: String? = null
        var participates: List<ParticipatesBean>? = null

        class ParticipatesBean {
            /**
             * approvalOrder : 0
             * chargeUser : string
             * contactWay : string
             * createTime : 2020-12-30T05:50:26.687Z
             * createUser : string
             * delFlag : string
             * id : string
             * orgId : string
             * orgName : string
             * projectId : string
             * superviseUser : string
             * type : string
             * updateTime : 2020-12-30T05:50:26.687Z
             * updateUser : string
             */
            var approvalOrder = 0
            var chargeUser: String? = null
            var contactWay: String? = null
            var createTime: String? = null
            var createUser: String? = null
            var delFlag: String? = null
            var id: String? = null
            var orgId: String? = null
            var orgName: String? = null
            var projectId: String? = null
            var superviseUser: String? = null
            var type: String? = null
            var updateTime: String? = null
            var updateUser: String? = null

        }
    }
}