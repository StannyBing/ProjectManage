package com.zx.projectmanage.module.projectapplication.construction.bean

import java.io.Serializable

/**
Date:2020/12/29
Time:3:49 PM
author:qingsong
 */

class DeviceListBean : Serializable {
    /**
     * createTime : 2020-12-31T09:18:33.308Z
     * createUser : string
     * delFlag : string
     * detailedId : string
     * detailedProId : string
     * equipmentId : string
     * equipmentName : string
     * id : string
     * latitude : string
     * longitude : string
     * name : string
     * postAddr : string
     * postDetails : [{"attachment":"string","createTime":"2020-12-31T09:18:33.308Z","createUser":"string","delFlag":"string","fileType":0,"postDetailId":"string","remarks":"string","standardProId":"string","stepId":"string","updateTime":"2020-12-31T09:18:33.308Z","updateUser":"string"}]
     * remarks : string
     * standardId : string
     * subProjectId : string
     */
    var createTime: String? = null
    var createUser: String? = null
    var delFlag: String? = null
    var detailedId: String? = null
    var detailedProId: String? = null
    var equipmentId: String? = null
    var equipmentName: String? = null
    var id: String? = null
    var latitude: String? = null
    var longitude: String? = null
    var name: String? = null
    var postAddr: String? = null
    var remarks: String? = null
    var standardId: String? = null
    var subProjectId: String? = null
    var postDetails: List<PostDetailsBean?>? = null


    class PostDetailsBean : Serializable {
        /**
         * attachment : string
         * createTime : 2020-12-31T09:18:33.308Z
         * createUser : string
         * delFlag : string
         * fileType : 0
         * postDetailId : string
         * remarks : string
         * standardProId : string
         * stepId : string
         * updateTime : 2020-12-31T09:18:33.308Z
         * updateUser : string
         */
        var attachment: String? = null
        var createTime: String? = null
        var createUser: String? = null
        var delFlag: String? = null
        var fileType = 0
        var postDetailId: String? = null
        var remarks: String? = null
        var standardProId: String? = null
        var stepId: String? = null
        var updateTime: String? = null
        var updateUser: String? = null

    }
}