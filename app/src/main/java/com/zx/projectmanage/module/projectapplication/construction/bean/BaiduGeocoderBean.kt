package com.zx.projectmanage.module.projectapplication.construction.bean

data class BaiduGeocoderBean(
    var status: Int,
    var message: String,
    var result: ResultBaen?
) {

    data class ResultBaen(var address: String?)

}