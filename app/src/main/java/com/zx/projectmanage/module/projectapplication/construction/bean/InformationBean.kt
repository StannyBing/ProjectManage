package com.zx.projectmanage.module.projectapplication.construction.bean

/**
Date:2020/12/30
Time:7:21 PM
author:qingsong
 */
class InformationBean {


    /**
     * projectId : 1344559609062363137
     * projectName : 挖矿
     * projectNumber : 20201231000021
     * mainProjectName : 斜体挖矿
     * tenders : 354795393491406848
     * buildPeriod : 354797950775332864
     * districtCode : 110000000000
     * projectAddress : 北京市
     * recordNo :
     * longitude : 116.397128
     * latitude : 39.916527
     * completedTime : 2020-12-31 16:22:09
     * assessmentId : 354378904456138752
     * score : null
     * projectStatus : 1
     * participates : [{"id":"1344559609100111873","projectId":"1344559609062363137","orgId":"354371231857774592","type":"1","orgName":"默认单位","chargeUser":"wl,测试,test4,test3,testAdmin,","superviseUser":"356960541731721216,355968735913185280,354843741304918016,354843658970730496,359076593332654080","contactWay":"","approvalOrder":3,"createTime":"2020-12-31 08:22:45","createUser":null,"updateTime":"2021-01-06 02:57:14","updateUser":null,"delFlag":"0"},{"id":"1344559609104306178","projectId":"1344559609062363137","orgId":"356161344350720000","type":"2","orgName":"测试监理单位","chargeUser":"测试确认密码用户,测试用户,测试监理方用户,","superviseUser":"356527929879957504,356245639534743552,356191548423016448","contactWay":"","approvalOrder":null,"createTime":"2020-12-31 08:22:45","createUser":null,"updateTime":"2021-01-06 02:57:14","updateUser":null,"delFlag":"0"},{"id":"1344559609104306179","projectId":"1344559609062363137","orgId":"356173667622719488","type":"4","orgName":"测试施工单位","chargeUser":"testWorker,吕,","superviseUser":"356676308580700160,356176821877739520","contactWay":"","approvalOrder":null,"createTime":"2020-12-31 08:22:45","createUser":null,"updateTime":"2021-01-06 02:57:14","updateUser":null,"delFlag":"0"},{"id":"1344559609108500481","projectId":"1344559609062363137","orgId":"356172018598547456","type":"3","orgName":"测试业主单位","chargeUser":"","superviseUser":"","contactWay":"","approvalOrder":null,"createTime":"2020-12-31 08:22:45","createUser":null,"updateTime":"2021-01-06 02:57:14","updateUser":null,"delFlag":"0"}]
     * projectSurvey : dsa
     * projectMeasures :
     * remarks : sdas
     * createTime : 2020-12-31 08:22:45
     * createUser : 1
     * updateTime : 2021-01-05 06:30:23
     * updateUser : null
     * delFlag : 0
     * finalJudgmen : null
     * subProjectCount : null
     * completeDistrict : null
     * statusDesc : 未开始
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
    var score: Any? = null
    var projectStatus = 0
    var projectSurvey: String? = null
    var projectMeasures: String? = null
    var remarks: String? = null
    var createTime: String? = null
    var createUser: String? = null
    var updateTime: String? = null
    var updateUser: Any? = null
    var delFlag: String? = null
    var finalJudgmen: Any? = null
    var subProjectCount: Any? = null
    var completeDistrict: Any? = null
    var statusDesc: String? = null
    var participates: List<ParticipatesBean>? = null

    class ParticipatesBean {
        /**
         * id : 1344559609100111873
         * projectId : 1344559609062363137
         * orgId : 354371231857774592
         * type : 1
         * orgName : 默认单位
         * chargeUser : wl,测试,test4,test3,testAdmin,
         * superviseUser : 356960541731721216,355968735913185280,354843741304918016,354843658970730496,359076593332654080
         * contactWay :
         * approvalOrder : 3
         * createTime : 2020-12-31 08:22:45
         * createUser : null
         * updateTime : 2021-01-06 02:57:14
         * updateUser : null
         * delFlag : 0
         */
        var id: String? = null
        var projectId: String? = null
        var orgId: String? = null
        var type: String? = null
        var orgName: String? = null
        var chargeUser: String? = null
        var superviseUser: String? = null
        var contactWay: String? = null
        var approvalOrder = 0
        var createTime: String? = null
        var createUser: Any? = null
        var updateTime: String? = null
        var updateUser: Any? = null
        var delFlag: String? = null

    }
}