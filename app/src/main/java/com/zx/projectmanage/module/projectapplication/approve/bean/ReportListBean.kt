package com.zx.projectmanage.module.projectapplication.approve.bean

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
    private var total = 0
    private var size = 0
    private var current = 0
    private var optimizeCountSql = false
    private var hitCount = false
    private var searchCount = false
    private var pages = 0
    private var records: List<RecordsBean?>? = null
    private var orders: List<*>? = null

    fun getTotal(): Int {
        return total
    }

    fun setTotal(total: Int) {
        this.total = total
    }

    fun getSize(): Int {
        return size
    }

    fun setSize(size: Int) {
        this.size = size
    }

    fun getCurrent(): Int {
        return current
    }

    fun setCurrent(current: Int) {
        this.current = current
    }

    fun isOptimizeCountSql(): Boolean {
        return optimizeCountSql
    }

    fun setOptimizeCountSql(optimizeCountSql: Boolean) {
        this.optimizeCountSql = optimizeCountSql
    }

    fun isHitCount(): Boolean {
        return hitCount
    }

    fun setHitCount(hitCount: Boolean) {
        this.hitCount = hitCount
    }

    fun isSearchCount(): Boolean {
        return searchCount
    }

    fun setSearchCount(searchCount: Boolean) {
        this.searchCount = searchCount
    }

    fun getPages(): Int {
        return pages
    }

    fun setPages(pages: Int) {
        this.pages = pages
    }

    fun getRecords(): List<RecordsBean?>? {
        return records
    }

    fun setRecords(records: List<RecordsBean?>?) {
        this.records = records
    }

    fun getOrders(): List<*>? {
        return orders
    }

    fun setOrders(orders: List<*>?) {
        this.orders = orders
    }

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
        var projectDesc: String? = null

    }
}