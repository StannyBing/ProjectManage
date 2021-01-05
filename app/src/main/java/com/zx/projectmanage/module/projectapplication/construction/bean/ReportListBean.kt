package com.zx.projectmanage.module.projectapplication.construction.bean

/**
Date:2020/12/29
Time:3:49 PM
author:qingsong
 */

class ReportListBean {

    /**
     * records : [{"projectId":"1341663839851696129","projectName":"通用项目","projectNumber":"日期-顺序编号（count+1）","mainProjectName":"测试项目","tenders":"标段枚举ID","buildPeriod":"建设期枚举","districtCode":"500000000000","projectAddress":"重庆市","recordNo":"备案号","longitude":"106.496346","latitude":"29.591109","completedTime":"2021-01-22 16:36:00","assessmentId":"考核模板id","score":0,"projectStatus":0,"participates":null,"projectSurvey":"","projectMeasures":"","remarks":"备注123","createTime":"2020-12-23 16:36:00","createUser":"admin","updateTime":"2020-12-25 04:07:09","updateUser":"","delFlag":"0","finalJudgmen":"","subProjectCount":1,"projectDesc":"未派工"}]
     * total : 1
     * size : 10
     * current : 1
     * orders : []
     * optimizeCountSql : true
     * hitCount : false
     * searchCount : true
     * pages : 1
     */
     var total = 0
     var size = 0
     var current = 0
     var optimizeCountSql = false
     var hitCount = false
     var searchCount = false
     var pages = 0
     var records: List<RecordsBean?>? = null
     var orders: List<*>? = null


    class RecordsBean {
        /**
         * projectId : 1341663839851696129
         * projectName : 通用项目
         * projectNumber : 日期-顺序编号（count+1）
         * mainProjectName : 测试项目
         * tenders : 标段枚举ID
         * buildPeriod : 建设期枚举
         * districtCode : 500000000000
         * projectAddress : 重庆市
         * recordNo : 备案号
         * longitude : 106.496346
         * latitude : 29.591109
         * completedTime : 2021-01-22 16:36:00
         * assessmentId : 考核模板id
         * score : 0
         * projectStatus : 0
         * participates : null
         * projectSurvey :
         * projectMeasures :
         * remarks : 备注123
         * createTime : 2020-12-23 16:36:00
         * createUser : admin
         * updateTime : 2020-12-25 04:07:09
         * updateUser :
         * delFlag : 0
         * finalJudgmen :
         * subProjectCount : 1
         * projectDesc : 未派工
         */
        var projectId: String? = null
        var projectName: String? = null
        var projectNumber: String? = null
        var mainProjectName: String? = null
        var tenders: String? = null
        var buildPeriod: String? = null
        var districtCode: String? = null
        var projectAddress: String? = null
        var recordNo: String? = null
        var longitude: String? = null
        var latitude: String? = null
        var completedTime: String? = null
        var assessmentId: String? = null
        var score = 0
        var projectStatus = 0
        var participates: Any? = null
        var projectSurvey: String? = null
        var projectMeasures: String? = null
        var remarks: String? = null
        var createTime: String? = null
        var createUser: String? = null
        var updateTime: String? = null
        var updateUser: String? = null
        var delFlag: String? = null
        var finalJudgmen: String? = null
        var subProjectCount = 0
        var desc: String? = null
        var statusDesc: String? = null

    }
}