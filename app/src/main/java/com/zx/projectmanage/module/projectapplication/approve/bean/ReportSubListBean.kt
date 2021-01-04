package com.zx.projectmanage.module.projectapplication.approve.bean

/**
Date:2020/12/29
Time:3:49 PM
author:qingsong
 */

class ReportSubListBean {

    /**
     * subProjectId : 1341711463145299969
     * projectId : 1341663839851696129
     * processId : 1341276250770563074
     * subProjectName : XXX隐患点
     * type : 默认类型（字典ID）
     * districtCode : 500000000000
     * address : 详细地址
     * longitude : 106.496346
     * latitude : 29.591109
     * status : 0
     * createUser : admin
     * updateUser : null
     * remarks : 备注
     * createTime : 2020-12-23 19:45:15
     * updateTime : 2020-12-25 08:39:01
     * beginTime : 2020-12-23 19:45:15
     * endTime : 2021-01-22 19:45:15
     * processCount : null
     * delFlag : 0
     * constructionTeam : 1
     * statusDesc : 未派工
     */
    private var subProjectId: String? = null
    private var projectId: String? = null
    private var processId: String? = null
    private var subProjectName: String? = null
    private var type: String? = null
    private var districtCode: String? = null
    private var address: String? = null
    private var longitude: String? = null
    private var latitude: String? = null
    private var status = 0
    private var createUser: String? = null
    private var updateUser: Any? = null
    private var remarks: String? = null
    private var createTime: String? = null
    private var updateTime: String? = null
    private var beginTime: String? = null
    private var endTime: String? = null
    private var processCount: Any? = null
    private var delFlag: String? = null
    private var constructionTeam: String? = null
    private var statusDesc: String? = null

    fun getSubProjectId(): String? {
        return subProjectId
    }

    fun setSubProjectId(subProjectId: String?) {
        this.subProjectId = subProjectId
    }

    fun getProjectId(): String? {
        return projectId
    }

    fun setProjectId(projectId: String?) {
        this.projectId = projectId
    }

    fun getProcessId(): String? {
        return processId
    }

    fun setProcessId(processId: String?) {
        this.processId = processId
    }

    fun getSubProjectName(): String? {
        return subProjectName
    }

    fun setSubProjectName(subProjectName: String?) {
        this.subProjectName = subProjectName
    }

    fun getType(): String? {
        return type
    }

    fun setType(type: String?) {
        this.type = type
    }

    fun getDistrictCode(): String? {
        return districtCode
    }

    fun setDistrictCode(districtCode: String?) {
        this.districtCode = districtCode
    }

    fun getAddress(): String? {
        return address
    }

    fun setAddress(address: String?) {
        this.address = address
    }

    fun getLongitude(): String? {
        return longitude
    }

    fun setLongitude(longitude: String?) {
        this.longitude = longitude
    }

    fun getLatitude(): String? {
        return latitude
    }

    fun setLatitude(latitude: String?) {
        this.latitude = latitude
    }

    fun getStatus(): Int {
        return status
    }

    fun setStatus(status: Int) {
        this.status = status
    }

    fun getCreateUser(): String? {
        return createUser
    }

    fun setCreateUser(createUser: String?) {
        this.createUser = createUser
    }

    fun getUpdateUser(): Any? {
        return updateUser
    }

    fun setUpdateUser(updateUser: Any?) {
        this.updateUser = updateUser
    }

    fun getRemarks(): String? {
        return remarks
    }

    fun setRemarks(remarks: String?) {
        this.remarks = remarks
    }

    fun getCreateTime(): String? {
        return createTime
    }

    fun setCreateTime(createTime: String?) {
        this.createTime = createTime
    }

    fun getUpdateTime(): String? {
        return updateTime
    }

    fun setUpdateTime(updateTime: String?) {
        this.updateTime = updateTime
    }

    fun getBeginTime(): String? {
        return beginTime
    }

    fun setBeginTime(beginTime: String?) {
        this.beginTime = beginTime
    }

    fun getEndTime(): String? {
        return endTime
    }

    fun setEndTime(endTime: String?) {
        this.endTime = endTime
    }

    fun getProcessCount(): Any? {
        return processCount
    }

    fun setProcessCount(processCount: Any?) {
        this.processCount = processCount
    }

    fun getDelFlag(): String? {
        return delFlag
    }

    fun setDelFlag(delFlag: String?) {
        this.delFlag = delFlag
    }

    fun getConstructionTeam(): String? {
        return constructionTeam
    }

    fun setConstructionTeam(constructionTeam: String?) {
        this.constructionTeam = constructionTeam
    }

    fun getStatusDesc(): String? {
        return statusDesc
    }

    fun setStatusDesc(statusDesc: String?) {
        this.statusDesc = statusDesc
    }
}