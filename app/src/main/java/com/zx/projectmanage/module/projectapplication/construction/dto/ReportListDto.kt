package com.zx.projectmanage.module.projectapplication.construction.dto

/**
Date:2020/12/30
Time:11:01 AM
author:qingsong
 */
data class ReportListDto(var pageNo:Int,var pageSize:Int,var keyword:String?=null,var projectStatus:Int,var districtCode:String)